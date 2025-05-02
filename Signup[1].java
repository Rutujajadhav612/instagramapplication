package instagram;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Signup extends JFrame implements ActionListener {

    private final JPanel contentPane;
    private final JTextField usernameField;
    private final JTextField nameField;
    private final JPasswordField passwordField;
    private final JTextField answerField;
    private final JButton createButton;
    private final JButton backButton;
    private final JComboBox<String> securityQuestionComboBox;

    public static void main(String[] args) {
        new Signup().setVisible(true);
    }

    public Signup() {
        setBounds(600, 250, 400, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null);
        
        JLabel l1 = new JLabel("");
        l1.setBounds(71, -12, 300, 100);
        ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("icons/ui.png"));
        Image img2 = img.getImage().getScaledInstance(l1.getWidth(), l1.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(img2);
        l1.setIcon(logoIcon);
        contentPane.add(l1);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.DARK_GRAY);
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUsername.setBounds(40, 116, 92, 26); // Adjusted Y-coordinate
        contentPane.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(40, 142, 320, 32); // Adjusted Y-coordinate
        contentPane.add(usernameField);
        usernameField.setColumns(10);

        JLabel lblName = new JLabel("Full Name:");
        lblName.setForeground(Color.DARK_GRAY);
        lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblName.setBounds(40, 190, 92, 26); // Adjusted Y-coordinate
        contentPane.add(lblName);

        nameField = new JTextField();
        nameField.setColumns(10);
        nameField.setBounds(40, 216, 320, 32); // Adjusted Y-coordinate
        contentPane.add(nameField);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.DARK_GRAY);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(40, 262, 92, 26); // Adjusted Y-coordinate
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setColumns(10);
        passwordField.setBounds(40, 288, 320, 32); // Adjusted Y-coordinate
        contentPane.add(passwordField);

        JLabel lblSecurityQuestion = new JLabel("Security Question:");
        lblSecurityQuestion.setForeground(Color.DARK_GRAY);
        lblSecurityQuestion.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSecurityQuestion.setBounds(40, 336, 140, 26); // Adjusted Y-coordinate
        contentPane.add(lblSecurityQuestion);

        securityQuestionComboBox = new JComboBox<>();
        securityQuestionComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Your NickName?", "Your Lucky Number?",
                "Your child SuperHero?", "Your childhood Name ?"}));
        securityQuestionComboBox.setBounds(40, 362, 320, 32); // Adjusted Y-coordinate
        contentPane.add(securityQuestionComboBox);

        JLabel lblAnswer = new JLabel("Security Answer:");
        lblAnswer.setForeground(Color.DARK_GRAY);
        lblAnswer.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAnswer.setBounds(40, 410, 140, 26); // Adjusted Y-coordinate
        contentPane.add(lblAnswer);

        answerField = new JTextField();
        answerField.setColumns(10);
        answerField.setBounds(40, 436, 320, 32); // Adjusted Y-coordinate
        contentPane.add(answerField);

        createButton = new JButton("Create Account");
        createButton.addActionListener(this);
        createButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        createButton.setBounds(40, 484, 150, 30); // Adjusted Y-coordinate
        createButton.setBackground(new Color(30, 144, 255));
        createButton.setForeground(Color.BLACK);
        contentPane.add(createButton);

        backButton = new JButton("Back to Login");
        backButton.addActionListener(this);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        backButton.setBounds(210, 484, 150, 30); // Adjusted Y-coordinate
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setForeground(Color.BLACK);
        contentPane.add(backButton);

        JPanel panel = new JPanel();
        panel.setForeground(new Color(34, 139, 34));
        panel.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 0), 2), "Welcome-To-Instagram",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(34, 139, 34)));
        panel.setBounds(30, 86, 350, 500); // Adjusted Y-coordinate
        panel.setBackground(Color.WHITE);
        contentPane.add(panel);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            Conn con = new Conn();

            if (ae.getSource() == createButton) {
                String sql = "INSERT INTO account(username, name, password, question, answer) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement st = con.c.prepareStatement(sql);

                st.setString(1, usernameField.getText());
                st.setString(2, nameField.getText());
                st.setString(3, new String(passwordField.getPassword()));
                st.setString(4, (String) securityQuestionComboBox.getSelectedItem());
                st.setString(5, answerField.getText());

                int i = st.executeUpdate();
                if (i > 0) {
                    JOptionPane.showMessageDialog(null, "Account Created Successfully");
                }

                usernameField.setText("");
                nameField.setText("");
                passwordField.setText("");
                answerField.setText("");
            }
            if (ae.getSource() == backButton) {
                this.setVisible(false);
                new Login().setVisible(true);
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
    }
}


