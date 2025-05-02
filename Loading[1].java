package instagram;
import java.awt.*;
import javax.swing.*;

public class Loading extends JFrame implements Runnable {

    private JPanel contentPane;
    private JProgressBar progressBar;
    private JLabel loadingLabel;
    private String username;
    private int s;
    private Thread th;

    public static void main(String[] args) {
        new Loading("").setVisible(true);
    }

    public void setUploading() {
        setVisible(false);
        th.start();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 200; i++) {
                s = s + 1;
                int m = progressBar.getMaximum();
                int v = progressBar.getValue();
                if (v < m) {
                    progressBar.setValue(progressBar.getValue() + 1);
                } else {
                    i = 201;
                    setVisible(false);
                    new Home(username).setVisible(true);
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Loading(String username) {
        this.username = username;
        th = new Thread(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(600, 300, 600, 400);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(51, 204, 255));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblInstagram = new JLabel("ChitChat");
        lblInstagram.setForeground(new Color(72, 209, 204));
        lblInstagram.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblInstagram.setBounds(170, 40, 300, 40);
        contentPane.add(lblInstagram);

        loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        loadingLabel.setForeground(new Color(160, 82, 45));
        loadingLabel.setBounds(270, 100, 100, 20);
        contentPane.add(loadingLabel);

        progressBar = new JProgressBar();
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressBar.setStringPainted(true);
        progressBar.setBounds(150, 150, 300, 30);
        contentPane.add(progressBar);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(10, 10, 580, 380);
        contentPane.add(panel);

        setUndecorated(true);
        setUploading();
    }
}
