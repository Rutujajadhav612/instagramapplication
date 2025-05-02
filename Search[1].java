package instagram;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Search extends JFrame {
    private Connection connection;
    private JTextField searchField;
    private JList<String> searchResultsList;

    public Search() {
        super("Search Account");

        // ... (Database connection setup remains the same)
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shiv", "root", "Shivendra9605");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
            return;
        }
        
        // Create UI components
        searchField = new JTextField(20);
        JButton searchButton = new JButton();
        
        ImageIcon c1 = new ImageIcon(ClassLoader.getSystemResource("icons/searchicon.png"));
        Image i1 = c1.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i1);
        searchButton.setIcon(i2);
        searchButton.setPreferredSize(new Dimension(20, 20));    
        searchResultsList = new JList<>();

        // Create a scroll pane for the search results list
        JScrollPane scrollPane = new JScrollPane(searchResultsList);
        

        // Style the search button
        searchButton.setBackground(new Color(59, 89, 182));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        // Add action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                List<String> searchResults = searchAccounts(searchText);
                displaySearchResults(searchResults);
            }
        });

        // Add ListSelectionListener to handle item selection
        searchResultsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedUsername = searchResultsList.getSelectedValue();
                    if (selectedUsername != null) {
                        // Open the profile using ViewProfile
                        openUserProfile(selectedUsername);
                    }
                }
            }
        });

        // Create and configure the main panel with a background color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setLayout(new BorderLayout());
        
        // Create a panel for the search field and button, and set their layout
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        mainPanel.add(searchPanel, BorderLayout.PAGE_START);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Set frame size and increase height (you can adjust the values)
        setSize(400, 600);
        setLocationRelativeTo(null);
        add(mainPanel);
        setVisible(true);
    }

    // ... (The rest of your code remains the same)
private List<String> searchAccounts(String searchText) {
        List<String> results = new ArrayList<>();
        try {
            String query = "SELECT username FROM account WHERE username LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(resultSet.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void displaySearchResults(List<String> results) {
        String[] resultArray = results.toArray(new String[0]);
        SwingUtilities.invokeLater(() -> {
            searchResultsList.setListData(resultArray);
        });
    }

    private void openUserProfile(String username) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewProfile(username,Login.loggedInuser);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Search();
        });
    }
}
