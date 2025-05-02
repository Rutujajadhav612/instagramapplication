package instagram;
import static instagram.Login.loggedInuser;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;

public class ViewProfile extends JFrame {
    private String username;
    private Connection connection;
    private JPanel profilePanel;
    private String loggedInUser;

    public ViewProfile(String username, String loggedInUser) {
        super("Profile");
        this.username = username;
        this.loggedInUser = loggedInUser;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shiv", "root", "Shivendra9605");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
            return;
        }

        profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        JLabel bio=new JLabel("Jai Hind..!!!");
        bio.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel twopanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton chatButton = new JButton();
        ImageIcon a1 = new ImageIcon(ClassLoader.getSystemResource("icons/chat.jpeg"));
        Image b1 = a1.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT);
        ImageIcon b2 = new ImageIcon(b1);
        chatButton.setIcon(b2);
        //viewPostsButton.setPreferredSize(new Dimension(40, 40));  
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for a password
                String password = JOptionPane.showInputDialog("Enter your password:");
                if (password != null) {
                    openChatWithUser(username, password);
                }
            }
        });

        ImageIcon profilePictureIcon = loadProfilePicture(username);
        JLabel profilePictureLabel = new JLabel(profilePictureIcon);
        profilePictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Load and display user's username
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton UnfollowButton = new JButton("UnFollow");
        UnfollowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        UnfollowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UnfollowUser();
            }
        });

        // Load and display user's follower and following counts
        JPanel countsPanel = new JPanel(new FlowLayout());
        int followersCount = getFollowersCount(username);
        int followingCount = getFollowingCount(username);
        JLabel followersLabel = new JLabel("Followers: " + followersCount);
        JLabel followingLabel = new JLabel("Following: " + followingCount);
        countsPanel.add(followersLabel);
        countsPanel.add(followingLabel);

        // Add a button to view uploaded posts
        JButton viewPostsButton = new JButton();
        ImageIcon c1 = new ImageIcon(ClassLoader.getSystemResource("icons/p.png"));
        Image i1 = c1.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i1);
        viewPostsButton.setIcon(i2);
        //viewPostsButton.setPreferredSize(new Dimension(40, 40));  
        viewPostsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        viewPostsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewUploadedPosts();
            }
        });

        JButton followButton = new JButton("Follow");
        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                followUser();
            }
        });
        
        // Add components to the profile panel
        profilePanel.add(profilePictureLabel);
        profilePanel.add(usernameLabel);
        profilePanel.add(countsPanel);
        profilePanel.add(bio);
        profilePanel.add(buttonPanel);
        profilePanel.add(twopanel);
 buttonPanel.add(followButton);
buttonPanel.add(UnfollowButton);
        twopanel.add(chatButton);
        twopanel.add(viewPostsButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        add(profilePanel);
        setVisible(true);
    }

    // Method to view uploaded posts
    private void viewUploadedPosts() {
        // Create a new window to display the uploaded posts
        JFrame uploadedPostsFrame = new ViewUploadedPost(username);
    }

    private ImageIcon loadProfilePicture(String username) {
        try {
            String query = "SELECT profile_picture FROM customer WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
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
        ImageIcon circularPredefinedIcon = createCircularIcon(predefinedIcon, 150); // Adjust size as needed
        return circularPredefinedIcon;
    }

    private void openChatWithUser(String selectedUser, String password) {
        String sender = findSenderForPassword(password);
        if (sender != null) {
            JFrame chatFrame = new ChatWindow(sender, selectedUser);
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect password. Chat access denied.");
        }
    }

    private String findSenderForPassword(String password) {
        try {
            String query = "SELECT username FROM account WHERE password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username"); // Return the username associated with the password
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Password not found in the database
    }

    private void followUser() {
        if (loggedInUser != null && !loggedInUser.isEmpty()) {
            if (loggedInUser.equals(username)) {
                JOptionPane.showMessageDialog(null, "You cannot follow yourself.");
                return;
            }

            try {
                String insertQuery = "INSERT INTO followers (follower, followee) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                preparedStatement.setString(1, loggedInUser);
                preparedStatement.setString(2, username);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "You are now following " + username);
                    // Update the follower and following counts
                    updateCounts();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to follow " + username);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: Unable to follow " + username);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please log in to follow this user.");
        }
    }
    private void UnfollowUser() {
    try {
        // Check if the logged-in user is trying to unfollow themselves
        if (loggedInUser.equals(username)) {
            JOptionPane.showMessageDialog(null, "You cannot unfollow yourself.");
            return; // Exit the method
        }

        // Debug: Print values of loggedInUser and username
        System.out.println("loggedInUser: " + loggedInUser);
        System.out.println("username: " + username);

        // Establish a connection to your database (assuming you have a 'Connection' object)
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shiv", "root", "Shivendra9605");

        // Prepare the SQL query to remove the follower relationship
        String deleteQuery = "DELETE FROM followers WHERE follower = ? AND followee = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

        // Set the values for the follower and followee
        preparedStatement.setString(1, loggedInUser);
        preparedStatement.setString(2, username);

        // Debug: Print the generated SQL query (for verification)
        System.out.println("SQL Query: " + preparedStatement.toString());

        // Execute the SQL query to remove the follower relationship
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "You have unfollowed " + username);
            Deletecount();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to unfollow " + username);
        }

        // Close the database connection
        connection.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: Unable to unfollow " + username);
    }
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
    private void updateCounts() {
    // Update the follower count for the user being followed
    int newFollowersCount = getFollowersCount(username) + 1;

    try {
        String updateQuery = "UPDATE follower_counts SET follower_count = ? WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

        preparedStatement.setInt(1, newFollowersCount);
        preparedStatement.setString(2, username);

        int rowsAffected = preparedStatement.executeUpdate();
        preparedStatement.close(); // Close the first statement

        if (rowsAffected > 0) {
            // Update the following count for the logged-in user
            int newFollowingCount = getFollowingCount(loggedInUser) + 1;

            // Create a new PreparedStatement for the second query
            updateQuery = "UPDATE follower_counts SET followee_count = ? WHERE username = ?";
            PreparedStatement followingCountStatement = connection.prepareStatement(updateQuery);

            followingCountStatement.setInt(1, newFollowingCount);
            followingCountStatement.setString(2, loggedInUser);

            int followingRowsAffected = followingCountStatement.executeUpdate();
            followingCountStatement.close(); // Close the second statement

            if (followingRowsAffected > 0) {
                // Successfully updated follower and following counts
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void Deletecount()  {
        int oldFollowersCount = getFollowersCount(username) - 1;
   
    try {
        String updateQuery = "UPDATE follower_counts SET follower_count = ? WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

        preparedStatement.setInt(1, oldFollowersCount);
        preparedStatement.setString(2, username);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            // Update the following count for the logged-in user
            int oldFollowingCount = getFollowingCount(loggedInUser) - 1;
      updateQuery = "UPDATE follower_counts SET followee_count = ? WHERE username = ?";
      preparedStatement = connection.prepareStatement(updateQuery);

preparedStatement.setInt(1, oldFollowingCount);
preparedStatement.setString(2, loggedInUser); // Use 'loggedInUser' here


            int followingRowsAffected = preparedStatement.executeUpdate();

            if (followingRowsAffected > 0) {
                // Successfully updated follower and following counts
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
private int getFollowersCount(String username) {
    try {
        String query = "SELECT follower_count FROM follower_counts WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("follower_count");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Return a placeholder count for now
    return 0;
}

private int getFollowingCount(String username) {
    try {
        String query = "SELECT followee_count FROM follower_counts WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("followee_count");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Return a placeholder count for now
    return 0;
}
public static void main(String[] args) {
    if (args.length < 2) {
        System.out.println("Usage: java ViewProfile <username> <loggedInUser>");
        System.exit(1);
    }

    SwingUtilities.invokeLater(() -> {
        new ViewProfile("username_of_user_to_view", "username_of_logged_in_user");
    });
    
 }
}

  





    

    
