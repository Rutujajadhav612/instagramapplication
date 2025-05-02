
package instagram;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

    private JPanel panel;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton loginButton, signUpButton, forgotPasswordButton;
    public static String loggedInuser;

    public Login() {

        setBackground(new Color(255, 255, 204));
        setBounds(550, 250, 400, 500);

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        setContentPane(panel);
        panel.setLayout(null);

        ImageIcon instagramIcon = new ImageIcon(ClassLoader.getSystemResource("icons/chitchat.png"));
        Image instagramImage = instagramIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(instagramImage);

        JLabel logoLabel = new JLabel(icon);
        logoLabel.setBounds(125, 30, 150, 150);
        panel.add(logoLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(40, 200, 90, 20);
        panel.add(usernameLabel);

        textField = new JTextField();
        textField.setBounds(140, 200, 200, 30);
        panel.add(textField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 250, 90, 20);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 250, 200, 30);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setForeground(new Color(0, 0, 255));
        loginButton.setBackground(new Color(0, 149, 246));
        loginButton.setBounds(40, 310, 300, 40);
        panel.add(loginButton);

        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(this);
        signUpButton.setForeground(new Color(0, 149, 246));
        signUpButton.setBackground(new Color(255, 255, 255));
        signUpButton.setBounds(40, 370, 300, 40);
        panel.add(signUpButton);

        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(this);
        forgotPasswordButton.setForeground(new Color(255, 0, 0));
        forgotPasswordButton.setBackground(new Color(255, 255, 255));
        forgotPasswordButton.setBounds(40, 430, 300, 40);
        panel.add(forgotPasswordButton);

        JLabel troubleLoginLabel = new JLabel("Trouble in Login?");
        troubleLoginLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        troubleLoginLabel.setForeground(new Color(0, 0, 0));
        troubleLoginLabel.setBounds(180, 480, 150, 20);
        panel.add(troubleLoginLabel);

        setTitle("ChitChat Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginButton) {
            Boolean status = false;
            try {
                Conn con = new Conn();
                String sql = "select * from account where username=? and password=?";
                PreparedStatement st = con.c.prepareStatement(sql);

                st.setString(1, textField.getText());
                st.setString(2, new String(passwordField.getPassword()));

                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    loggedInuser=textField.getText();
                    this.setVisible(false);
                    new Loading(textField.getText()).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Login or Password!");
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (ae.getSource() == signUpButton) {
            setVisible(false);
            Signup signUp = new Signup();
            signUp.setVisible(true);
        }
        if (ae.getSource() == forgotPasswordButton) {
            setVisible(false);
            ForgotPassword forgot = new ForgotPassword();
            forgot.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}
