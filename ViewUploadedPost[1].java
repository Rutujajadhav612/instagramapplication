package instagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class ViewUploadedPost extends JFrame {
    private JPanel panel;
    private String username;
    private String loggedInUser;
    private final int IMAGE_WIDTH = 300; // Adjust the desired image width
    private final int IMAGE_HEIGHT = 300; // Adjust the desired image height

    public ViewUploadedPost(String username) {
        super("View Posts");
        this.username = username; // owner of post
        this.loggedInUser = Login.loggedInuser; // get the logged-in user

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        getContentPane().add(scrollPane);

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");
            String query = "SELECT p.id, p.image, p.caption, COUNT(pl.post_id) AS like_count " +
                    "FROM post p " +
                    "LEFT JOIN post_likes pl ON p.id = pl.post_id " +
                    "WHERE p.username = ? " +
                    "GROUP BY p.id";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int postId = resultSet.getInt("id");
                Blob imageBlob = resultSet.getBlob("image");
                String caption = resultSet.getString("caption");
                int likeCount = resultSet.getInt("like_count");

                ImageIcon originalImageIcon = new ImageIcon(imageBlob.getBytes(1, (int) imageBlob.length()));

                Image originalImage = originalImageIcon.getImage();
                Image resizedImage = originalImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

                JLabel imageLabel = new JLabel(resizedImageIcon, JLabel.CENTER);
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Create buttons for Like and Comment
                JButton likeButton = new JButton();
                ImageIcon a1 = new ImageIcon(ClassLoader.getSystemResource("icons/li.png"));
                Image b1 = a1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
                ImageIcon b2 = new ImageIcon(b1);
                likeButton.setIcon(b2);

                JButton commentButton = new JButton();
                ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/ci.png"));
                Image i2 = i1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
                ImageIcon i3 = new ImageIcon(i2);
                commentButton.setIcon(i3);

                // Create a JLabel to display the like count
                JLabel likeCountLabel = new JLabel("Likes: " + likeCount);

                // Add action listeners to Like and Comment buttons
                likeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        likePost(postId, likeCountLabel, likeButton);
                    }
                });

                commentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String commentText = JOptionPane.showInputDialog(ViewUploadedPost.this, "Enter your comment:");
                        if (commentText != null && !commentText.isEmpty()) {
                            commentOnPost(postId, commentText);
                        }
                    }
                });

                // Create a panel to hold the Like and Comment buttons
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(likeButton);
                buttonPanel.add(commentButton);

                // Add some vertical space between images and buttons
                panel.add(Box.createVerticalStrut(20));
                panel.add(imageLabel);
                panel.add(buttonPanel);

                // Display the like count
                panel.add(likeCountLabel);
            }

            resultSet.close();
            preparedStatement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void likePost(int postId, JLabel likeCountLabel, JButton likeButton) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");

            String checkQuery = "SELECT * FROM post_likes WHERE post_id = ? AND username = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkQuery);
            checkStatement.setInt(1, postId);
            checkStatement.setString(2, loggedInUser);

            ResultSet checkResult = checkStatement.executeQuery();

            if (!checkResult.next()) {
                String insertQuery = "INSERT INTO post_likes (post_id, username) VALUES (?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(insertQuery);
                insertStatement.setInt(1, postId);
                insertStatement.setString(2, loggedInUser);
                int rowsAffected = insertStatement.executeUpdate();
                insertStatement.close();

                if (rowsAffected > 0) {
                    int currentLikes = getLikeCount(postId);
                    likeCountLabel.setText("Likes: " + currentLikes);
                    likeButton.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to like the post.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "You have already liked this post.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            checkResult.close();
            checkStatement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getLikeCount(int postId) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");
            String query = "SELECT COUNT(*) AS like_count FROM post_likes WHERE post_id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int likeCount = resultSet.getInt("like_count");
                return likeCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void commentOnPost(int postId, String commentText) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");
            String insertQuery = "INSERT INTO post_comments (post_id, username, comment_text) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setInt(1, postId);
            preparedStatement.setString(2, loggedInUser);
            preparedStatement.setString(3, commentText);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewUploadedPost("your_username");
        });
    }
}

