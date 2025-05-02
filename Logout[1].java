/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instagram;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author vips_mac
 */
public class Logout extends JFrame {

    public Logout()        
    {
        JButton logout=new JButton("LOGOUT");
       // logout.setName("LOGOUT");
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // Close or hide the current JFrame when the logout button is clicked
               Home home=new Home();
            }
        }) ;   
        add(logout);
        setSize(200,100);
        setLocationRelativeTo(null);
        setTitle("Logout Example");
        setVisible(true);
        
    }
   public static void main(String args[])
   {
       
        SwingUtilities.invokeLater(() -> {
            Logout logout = new Logout();
            // Call the logout method to create a new Home frame
            
        });
   }
    
}
