package aoopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private UserManager userManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JCheckBox searchableCheckbox;
    private JButton registerButton;
    private JButton backButton;

    public RegisterPanel(MainFrame mainFrame, UserManager userManager) {
        this.mainFrame = mainFrame;
        this.userManager = userManager;
        setLayout(new GridBagLayout());

        // Set background color to a light blue
        setBackground(new Color(173, 216, 230));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        fullNameField = new JTextField(20);
        searchableCheckbox = new JCheckBox("Searchable");
        searchableCheckbox.setBackground(Color.red);
        searchableCheckbox.setForeground(Color.WHITE);
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 102, 204)); // Dark blue background
        registerButton.setForeground(Color.WHITE);
        backButton = new JButton("Back");
        backButton.setBackground(new Color(0, 102, 204)); // Dark blue background
        backButton.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        add(fullNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(searchableCheckbox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(registerButton, gbc);
        gbc.gridx = 1;
        add(backButton, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String fullName = fullNameField.getText();
                boolean isSearchable = searchableCheckbox.isSelected();
                if (!username.isEmpty() && !password.isEmpty() && !fullName.isEmpty()) {
                    User user = new User(username, password, fullName, isSearchable);
                    userManager.addUser(user);
                    mainFrame.switchTo("Login");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "All fields must be filled out.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchTo("Login");
            }
        });
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0, 102, 204)); // Dark blue color for text
        return label;
    }
}
