/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoopproject;

/**
 * Main frame of the application.
 * Uses ScrollDecorator to add scrolling capability to various panels.
 */
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UserManager userManager;
    private HomePanel homePanel;
    private ProfilePanel profilePanel;
    private SearchPanel searchPanel;

    public MainFrame(UserManager userManager) {
        this.userManager = userManager;
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this, userManager);
        RegisterPanel registerPanel = new RegisterPanel(this, userManager);
        homePanel = new HomePanel(this, userManager);
        profilePanel = new ProfilePanel(this, userManager);
        searchPanel = new SearchPanel(this, userManager);

        mainPanel.add(ScrollDecorator.decorate(loginPanel), "Login");
        mainPanel.add(ScrollDecorator.decorate(registerPanel), "Register");
        mainPanel.add(ScrollDecorator.decorate(homePanel), "Home");
        mainPanel.add(ScrollDecorator.decorate(profilePanel), "Profile");
        mainPanel.add(ScrollDecorator.decorate(searchPanel), "Search");

        add(mainPanel);
        setTitle("Social Media App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void switchTo(String card) {
        cardLayout.show(mainPanel, card);
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }

    public ProfilePanel getProfilePanel() {
        return profilePanel;
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }
}
