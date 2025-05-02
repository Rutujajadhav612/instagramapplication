package instagram;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UploadPost extends JFrame {
    private JButton uploadButton;
    private JButton browseButton;
    private JLabel imageLabel;
    private JTextArea captionTextArea;
    private String imagePath;
    private String username; 

    public UploadPost(String username) {
        super("Upload Post");
        this.username = username;

        // Set background color
        getContentPane().setBackground(new Color(255, 255, 255)); // White background

        // Create and customize components
        uploadButton = new JButton("Upload");
        uploadButton.setBounds(200, 300, 100, 40);
        uploadButton.setFont(new Font("Arial", Font.PLAIN, 14));

        browseButton = new JButton("Browse");
        browseButton.setBounds(80, 300, 100, 40);
        browseButton.setFont(new Font("Arial", Font.PLAIN, 14));

        imageLabel = new JLabel();
        imageLabel.setBounds(10, 10, 670, 250);

        captionTextArea = new JTextArea("Caption", 5, 30);
        JScrollPane captionScrollPane = new JScrollPane(captionTextArea);
        captionScrollPane.setBounds(350, 300, 300, 40);
        captionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Button to browse the image
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.gif, *.png)", "jpg", "gif", "png");
                fileChooser.addChoosableFileFilter(filter);
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    imageLabel.setIcon(ResizeImage(path));
                    imagePath = path;
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("No Data");
                }
            }
        });

        // Button to insert image and data into MySQL database
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql:///shiv", "root", "Shivendra9605");
                    PreparedStatement ps = con.prepareStatement("INSERT INTO post(username, image, caption) VALUES (?, ?, ?)");
                    InputStream is = new FileInputStream(new File(imagePath));
                    ps.setString(1, username);
                    ps.setBlob(2, is);
                    ps.setString(3, captionTextArea.getText());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data Inserted");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Add components to the frame
        add(imageLabel);
        add(captionScrollPane);
        add(browseButton);
        add(uploadButton);

        // Configure frame properties
        setLayout(null);
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window only, not the entire application
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    // Method to resize the image icon
    public ImageIcon ResizeImage(String imgPath) {
        ImageIcon MyImage = new ImageIcon(imgPath);
        Image img = MyImage.getImage();
        Image newImage = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UploadPost("username");
        });
    }
}
