
package instagram;
import java.awt.*;
import javax.swing.*;

public class Splash {
    public static void main(String[] args){
        SplashFrame f1 = new SplashFrame();
        f1.setVisible(true);
        int i;
        int x=1;
        for(i=2; i<=600; i+=10, x+=7){
            f1.setLocation(800 - ((i+x)/2),400 - (i/2));
            f1.setSize(i+x,i);
            try{
                Thread.sleep(10);
            }catch(InterruptedException e){}
        }
        
    }
}
class SplashFrame extends JFrame implements Runnable{
    Thread t1;
    SplashFrame(){
        setLayout(new FlowLayout());
        ImageIcon c1 = new ImageIcon(ClassLoader.getSystemResource("icons/insta.jpeg"));
        Image i1 = c1.getImage().getScaledInstance(1030, 600,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i1);
        
        JLabel l1 = new JLabel(i2);
        add(l1);
        setUndecorated(true);
        t1 = new Thread(this);
        t1.start();
    }
    @Override
    public void run(){
        try{
            Thread.sleep(7000);
            this.setVisible(false);
            
            Login l = new Login();
            l.setVisible(true);
        }catch(InterruptedException e){
           e.printStackTrace();
        }
    }
}
//"/Users/vips_mac/NetBeansProjects/Travel Management System/src/travel/management/system/Splash.java"