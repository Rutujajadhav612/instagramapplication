package instagram;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import static instagram.Login.loggedInuser;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Home extends JFrame {
    private String loggedInUser;
    private final String username;
    private final int IMAGE_WIDTH = 600;
    private final int IMAGE_HEIGHT = 400;

    public static void main(String[] args) {
        new Home("").setVisible(true);
    }

    public Home(String username) {
        super("ChitChat");
        setForeground(Color.CYAN);
        setLayout(null);
        this.username = username; // owner of post
        this.loggedInUser = Login.loggedInuser;

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        ImageIcon profilePictureIcon = loadProfilePicture(username);
        JLabel profilePictureLabel = new JLabel(profilePictureIcon);
        profilePictureLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu m1 = new JMenu("PROFILE");
        ImageIcon a1 = new ImageIcon(ClassLoader.getSystemResource("icons/home.png"));
        Image b1 = a1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon b2 = new ImageIcon(b1);
        m1.setIcon(b2);
        m1.setForeground(Color.BLUE);
        menuBar.add(m1);

        JMenuItem mi1 = new JMenuItem("ADD DETAILS");
        m1.add(mi1);

        JMenuItem mi2 = new JMenuItem("UPDATE DETAILS");
        m1.add(mi2);
       

        JMenuItem mi3 = new JMenuItem("VIEW PROFILE");
        m1.add(mi3);

        JMenuItem mi4 = new JMenuItem("DELETE DETAILS");
        m1.add(mi4);

        mi1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new AddUser(username).setVisible(true);
                } catch (SQLException e) {
                }
            }
        });

        mi2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new UpdateDetails(username).setVisible(true);
                } catch (Exception e) {
                }
            }
        });

        mi3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new ViewProfile(username, loggedInUser).setVisible(true);
                } catch (Exception e) {
                }
            }
        });

        mi4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new DeleteDetails().setVisible(true);
                } catch (Exception e) {
                }
            }
        });

        JMenu m16 = new JMenu("SEARCH");
        ImageIcon c1 = new ImageIcon(ClassLoader.getSystemResource("icons/se.png"));
        Image p1 = c1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon p2 = new ImageIcon(p1);
        m16.setIcon(p2);
        m16.setForeground(Color.RED);
        menuBar.add(m16);

        JMenuItem mi11 = new JMenuItem("SEARCH");
        mi11.setForeground(Color.BLUE);
        menuBar.add(m16);
        m16.add(mi11);
        mi11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new Search().setVisible(true);
                } catch (Exception e) {
                }
            }
        });

        JMenu m2 = new JMenu("CHAT");
        ImageIcon j1 = new ImageIcon(ClassLoader.getSystemResource("icons/send.png"));
        Image j2 = j1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon j3 = new ImageIcon(j2);
        m2.setIcon(j3);
        m2.setForeground(Color.RED);
        menuBar.add(m2);

        JMenuItem mi6 = new JMenuItem("CHAT");
        m2.add(mi6);

        mi6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new ChatWindow(Login.loggedInuser, username).setVisible(true);
                } catch (Exception e) {
                }
            }
        });

        JMenu m3 = new JMenu("MYPOST");
        m3.setForeground(Color.BLUE);
        ImageIcon l1 = new ImageIcon(ClassLoader.getSystemResource("icons/save.png"));
        Image l2 = l1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon l3 = new ImageIcon(l2);
        m3.setIcon(l3);
        menuBar.add(m3);

        JMenuItem mi8 = new JMenuItem("UPLOAD NEW POST");
        m3.add(mi8);

        JMenuItem mi10 = new JMenuItem("VIEW UPLOADED POST");
        m3.add(mi10);

        mi8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new UploadPost(username).setVisible(true);
                } catch (Exception e) {
                }
            }

        });
        mi10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new ViewUploadedPost(username).setVisible(true);
                } catch (Exception e) {
                }
            }
        });
        m3.setForeground(Color.BLUE);
        menuBar.add(m3);

       

        JMenu m10 = new JMenu("SETTING");
        m10.setForeground(Color.BLUE);
        ImageIcon k1 = new ImageIcon(ClassLoader.getSystemResource("icons/sett.png"));
        Image k2 = k1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        ImageIcon k3 = new ImageIcon(k2);
        m10.setIcon(k3);
        menuBar.add(m10);

        JMenuItem mi12 = new JMenuItem("LOGOUT");
        m10.add(mi12);

        JMenuItem mi13 = new JMenuItem("HELP");
        m10.add(mi13);

        JMenuItem mi14 = new JMenuItem("REPORT PROBLEM");
        m10.add(mi14);

        mi12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });

        mi13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new Help().setVisible(true);
            }
        });

        mi14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new ReportProblem().setVisible(true);
            }
        });

        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        JScrollPane postScrollPane = new JScrollPane(postPanel);
        postScrollPane.setBounds(30, 30, 750, 800);
        postScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(postScrollPane, BorderLayout.CENTER);

        loadAndDisplayPosts(postPanel);
    }
 private void loadAndDisplayPosts(JPanel postPanel) {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");
        String query = "SELECT p.id, p.username, p.image, p.caption, COUNT(pl.post_id) AS like_count " +
                "FROM post p " +
                "LEFT JOIN post_likes pl ON p.id = pl.post_id " +
                "WHERE p.username IN (SELECT followee FROM followers WHERE follower = ?) " +
                "GROUP BY p.id";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, loggedInUser);  

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int postId = resultSet.getInt("id");
            Blob imageBlob = resultSet.getBlob("image");
            String username = resultSet.getString("username");  
            int likeCount = resultSet.getInt("like_count");

            ImageIcon profilePictureIcon = loadProfilePicture(username);
        JLabel profilePictureLabel = new JLabel(profilePictureIcon);
       // profilePictureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create a new JPanel for each post
       JPanel postContentPanel = new JPanel();
        postContentPanel.setLayout(new BoxLayout(postContentPanel, BoxLayout.Y_AXIS));

        // Create a panel to hold the profile picture and username
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.X_AXIS));
        

        JLabel ownerLabel = new JLabel(username);
        ownerLabel.setForeground(Color.BLACK);
        ownerLabel.setFont(new Font("Tahoma", Font.ITALIC, 20));
        //ownerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profilePanel.add(profilePictureLabel);
        profilePanel.add(Box.createRigidArea(new Dimension (5,5)));
        profilePanel.add(ownerLabel);
 
        postContentPanel.add(profilePanel);
           
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
                    String commentText = JOptionPane.showInputDialog(Home.this, "Enter your comment:");
                    if (commentText != null && !commentText.isEmpty()) {
                        commentOnPost(postId, commentText);
                    }
                }
            });

            // Create a panel to hold the Like and Comment buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(likeButton);
            buttonPanel.add(commentButton);

            // Add some vertical space between buttons and like count
            postContentPanel.add(Box.createVerticalStrut(20));

            // Add the image label, button panel, and like count to the post content panel
            postContentPanel.add(imageLabel);
            postContentPanel.add(buttonPanel);
            postContentPanel.add(likeCountLabel);

            // Add the post content panel to the main post panel
            postPanel.add(postContentPanel);
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

  private ImageIcon loadProfilePicture(String username) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");
            String query = "SELECT profile_picture FROM account WHERE username = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                byte[] imageData = resultSet.getBytes("profile_picture");
                if (imageData != null) {
                    ImageIcon originalIcon = new ImageIcon(imageData);
                    // Create a circular image
                    ImageIcon circularIcon = createCircularIcon(originalIcon, 150); // Adjust size as needed
                    return circularIcon;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return a predefined circular image if the profile picture is null or not available
        ImageIcon predefinedIcon = new ImageIcon(ClassLoader.getSystemResource("icons/homeinsta.jpg")); // Replace with the path to your predefined image
        ImageIcon circularPredefinedIcon = createCircularIcon(predefinedIcon, 25); // Adjust size as needed
        return circularPredefinedIcon;
    }
private ImageIcon createCircularIcon(ImageIcon originalIcon, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Create a circular mask
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
        g2d.setClip(circle);

        // Draw the original image with the circular mask
        originalIcon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();

        return new ImageIcon(image);
    }


    private void likePost(int postId, JLabel likeCountLabel, JButton likeButton) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");

            String checkQuery = "SELECT * FROM post_likes WHERE post_id = ? AND username = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkQuery);
            checkStatement.setInt(1, postId);
            checkStatement.setString(2, username);

            ResultSet checkResult = checkStatement.executeQuery();

            if (!checkResult.next()) {
                String insertQuery = "INSERT INTO post_likes (post_id, username) VALUES (?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(insertQuery);
                insertStatement.setInt(1, postId);
                insertStatement.setString(2, username);
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
                        preparedStatement.setString(2, username);
            preparedStatement.setString(3, commentText);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
