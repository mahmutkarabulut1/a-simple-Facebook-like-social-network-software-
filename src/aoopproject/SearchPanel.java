package aoopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel {
    private MainFrame mainFrame;
    private UserManager userManager;
    private JTextField searchField;
    private JTextArea resultArea;
    private JButton searchButton;
    private JButton backButton;
    private JPanel resultPanel;

    public SearchPanel(MainFrame mainFrame, UserManager userManager) {
        this.mainFrame = mainFrame;
        this.userManager = userManager;
        setLayout(new BorderLayout());

        setBackground(new Color(173, 216, 230)); // Light Blue
        
        searchField = new JTextField(20);
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        
        searchButton = new JButton("Search");
        resultArea.setBackground(Color.WHITE);
        resultArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Dark blue border
        
        backButton = new JButton("Back");
        backButton.setBackground(new Color(0, 102, 204)); // Dark blue 
        backButton.setForeground(Color.WHITE);
        
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Dark blue border


        JPanel searchPanel = new JPanel();
         searchPanel.setBackground(new Color(173, 216, 230)); // Light blue 
        searchPanel.add(new JLabel("Search User:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JScrollPane scrollPane = new JScrollPane(resultPanel);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                resultPanel.removeAll();
                for (User user : userManager.getSearchableUsers()) {
                    if (user.getUsername().contains(query) || user.getFullName().contains(query)) {
                        JTextArea userInfo = new JTextArea();
                        userInfo.setText("Username: " + user.getUsername() + "\nFull Name: " + user.getFullName() + "\nFollowers: " + user.getFollowManager().getFollowers().size() + "\nFollowing: " + user.getFollowManager().getFollowing().size());
                        userInfo.setEditable(false);

                        JButton sendFriendRequestButton = new JButton("Send Friend Request");
                        sendFriendRequestButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                userManager.getLoggedInUser().getFriendManager().sendFriendRequest(userManager.getLoggedInUser(), user);
                                JOptionPane.showMessageDialog(mainFrame, "Friend request sent to " + user.getUsername());
                            }
                        });

                        JButton followButton = new JButton("Follow");
                        followButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                userManager.getLoggedInUser().getFollowManager().follow(userManager.getLoggedInUser(), user);
                                JOptionPane.showMessageDialog(mainFrame, "Now following " + user.getUsername());
                            }
                        });

                        JPanel userPanel = new JPanel();
                        userPanel.setLayout(new BorderLayout());
                        userPanel.setBackground(new Color(173, 216, 230)); // Light blue 
                        userPanel.add(new JScrollPane(userInfo), BorderLayout.CENTER);
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.setBackground(new Color(173, 216, 230)); // Light blue 
                        buttonPanel.add(sendFriendRequestButton);
                        buttonPanel.add(followButton);
                        userPanel.add(buttonPanel, BorderLayout.SOUTH);

                        resultPanel.add(userPanel);
                    }
                }
                resultPanel.revalidate();
                resultPanel.repaint();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchTo("Home");
            }
        });
    }
}
