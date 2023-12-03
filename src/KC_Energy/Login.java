package KC_Energy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public Login() {
        // Set up the main window
        setTitle("Login - Kansas City Energy");
        setSize(400, 300); // increased the size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set up the form panel
        JPanel formPanel = new JPanel(new GridBagLayout()); // changed the layout manager to GridBagLayout
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5); // added padding to the components
        
        JLabel usernameLabel = new JLabel("Username:");
        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(usernameLabel, c);
        
        usernameField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 0;
        formPanel.add(usernameField, c);
        
        JLabel passwordLabel = new JLabel("Password:");
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(passwordLabel, c);
        
        passwordField = new JPasswordField(20);
        c.gridx = 1;
        c.gridy = 1;
        formPanel.add(passwordField, c);
        
        loginButton = new JButton("Login");
        c.gridx = 1;
        c.gridy = 2;
        formPanel.add(loginButton, c);
        
        // Add empty panels to the top and bottom of the form panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Kansas City Energy");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);
        topPanel.setPreferredSize(new Dimension(0, 60)); // added a bit more space
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(0, 30));
        
        // Add the panels to the content pane
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
        // Add a listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the input values from the text fields
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                // Check if the username and password are correct
                if (username.equals("abdi") && password.equals("123")) {
                    // Successful login
                	
                    JOptionPane.showMessageDialog(Login.this, "Login successful!");
                	GUI GUI = new GUI();
                } else {
                    // Incorrect login
                    JOptionPane.showMessageDialog(Login.this, "Incorrect username or password.");
                }
                
                // Clear the input fields
                usernameField.setText("");
                passwordField.setText("");
            }
        });
    }
    
    public static void main(String[] args) {
        Login loginPage = new Login();
        loginPage.setVisible(true);
    }
}
