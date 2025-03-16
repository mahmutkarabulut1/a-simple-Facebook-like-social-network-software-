package aoopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Panel for displaying the home screen, including posts from friends and the user.
 * Implements Observer to update the feed when the PostManager notifies of changes.
 */
public class HomePanel extends JPanel implements Observer {
    private MainFrame mainFrame;
    private UserManager userManager;
    private JTextArea postArea;
    private JButton postButton;
    private JButton logoutButton;
    private JPanel feedPanel; // feedArea yerine JPanel kullanacağız
    private JButton profileButton;
    private JButton searchButton;
    private JButton friendRequestsButton;
    private JLabel homeLabel;

    public HomePanel(MainFrame mainFrame, UserManager userManager) {
        this.mainFrame = mainFrame;
        this.userManager = userManager;

        setLayout(new BorderLayout());

        
        setBackground(new Color(173, 216, 230)); // Light blue

        homeLabel = new JLabel("Home");
        homeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        homeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homeLabel.setForeground(new Color(0, 102, 204)); // Dark blue text

        postArea = new JTextArea(3, 50);
        postArea.setBackground(Color.WHITE);
        postArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Dark blue border

        postButton = new JButton("Post");
        postButton.setBackground(new Color(0, 102, 204)); // Dark blue 
        postButton.setForeground(Color.WHITE);

        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(0, 102, 204)); // Dark blue 
        logoutButton.setForeground(Color.WHITE);

        profileButton = new JButton("Profile");
        profileButton.setBackground(new Color(0, 102, 204)); // Dark blue 
        profileButton.setForeground(Color.WHITE);

        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0, 102, 204)); // Dark blue 
        searchButton.setForeground(Color.WHITE);

        friendRequestsButton = new JButton("Friend Requests");
        friendRequestsButton.setBackground(new Color(0, 102, 204)); // Dark blue 
        friendRequestsButton.setForeground(Color.WHITE);

        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.setBackground(new Color(173, 216, 230)); // Light blue 
        postPanel.add(new JScrollPane(postArea), BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(173, 216, 230)); // Light blue 
        buttonPanel.add(profileButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(friendRequestsButton);
        buttonPanel.add(logoutButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(173, 216, 230)); // Light blue 
        topPanel.add(homeLabel, BorderLayout.NORTH);
        topPanel.add(postPanel, BorderLayout.CENTER);

        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setBackground(Color.WHITE);
        feedPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Dark blue
        JScrollPane feedScrollPane = new JScrollPane(feedPanel);
        feedScrollPane.setPreferredSize(new Dimension(800, 600)); 

        topPanel.add(feedScrollPane, BorderLayout.SOUTH); // feedArea yerine feedPanel ekledik

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(173, 216, 230)); // Light blue
        mainContent.add(topPanel, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // delete for extra scrollbutton
        scrollPane.getViewport().setBackground(new Color(173, 216, 230)); // Light blue background

        add(scrollPane, BorderLayout.CENTER);

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = postArea.getText();
                if (!content.isEmpty()) {
                    User loggedInUser = userManager.getLoggedInUser();
                    Post newPost = new Post(content, loggedInUser);
                    loggedInUser.getPostManager().addPost(newPost);
                    postArea.setText("");
                    userManager.saveUser(loggedInUser); // Save user after adding a post
                    updateFeed(); // Update feed after adding a post
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchTo("Login");
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.getProfilePanel().displayProfile(userManager.getLoggedInUser());
                mainFrame.switchTo("Profile");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.switchTo("Search");
            }
        });

        friendRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayFriendRequests();
            }
        });

        if (userManager.getLoggedInUser() != null) {
            // Add HomePanel as an observer to the logged-in user's PostManager.
            userManager.getLoggedInUser().getPostManager().addObserver(this);
            updateFeed(); // Download post
        }
    }

    @Override
    public void update() { // Updates the feed to display latest post when notifies observer change
        updateFeed();
    }

    public void updateFeed() {
        feedPanel.removeAll(); // clear feedPanel
        User loggedInUser = userManager.getLoggedInUser();
        if (loggedInUser != null) {
            List<Post> allPosts = new ArrayList<>();
            for (User friend : loggedInUser.getFriendManager().getFriends()) {
                allPosts.addAll(friend.getPostManager().getPosts());
            }
            allPosts.addAll(loggedInUser.getPostManager().getPosts());
            Collections.sort(allPosts, Comparator.comparing(Post::getTimestamp).reversed());

            for (Post post : allPosts) {
                addPostToFeed(post);
            }

            feedPanel.revalidate();
            feedPanel.repaint();
        }
    }

    private void addPostToFeed(Post post) {
        JPanel postPanel = new JPanel(new BorderLayout());
        JLabel postLabel = new JLabel(post.getUser().getFullName() + ": " + post.getContent() + " (" + post.getLikeCount() + " likes)");
        JButton likeButton = new JButton("Like");

        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                post.incrementLikes();
                userManager.saveUser(post.getUser());
                postLabel.setText(post.getUser().getFullName() + ": " + post.getContent() + " (" + post.getLikeCount() + " likes)");
                feedPanel.revalidate();
                feedPanel.repaint();
            }
        });

        postPanel.add(postLabel, BorderLayout.CENTER);
        postPanel.add(likeButton, BorderLayout.EAST);
        postPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        feedPanel.add(postPanel);
    }

    public void displayFriendRequests() {
        User loggedInUser = userManager.getLoggedInUser();
        if (loggedInUser != null) {
            List<FriendRequest> friendRequests = loggedInUser.getFriendManager().getReceivedFriendRequests();
            if (friendRequests.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "No friend requests.");
            } else {
                for (FriendRequest request : new ArrayList<>(friendRequests)) {
                    int result = JOptionPane.showConfirmDialog(mainFrame, "Accept friend request from " + request.getSender().getUsername() + "?", "Friend Request", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        loggedInUser.getFriendManager().acceptFriendRequest(request, userManager);
                        loggedInUser.getFriendManager().removeFriendRequest(request);
                        updateFriendsList(loggedInUser, request.getSender());
                    }
                }
            }
        }
    }

    private void updateFriendsList(User user1, User user2) {
        // Update the user objects in memory
        user1.getFriendManager().addFriend(user2);
        user2.getFriendManager().addFriend(user1);

        // Save the updated user objects
        userManager.saveBothUsers(user1, user2);

        // If either user is the logged-in user, update the UI
        if (userManager.getLoggedInUser().equals(user1) || userManager.getLoggedInUser().equals(user2)) {
            updateFeed();
        }
    }
}



