package aoopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for displaying a user's profile.
 * Implements Observer to update the profile when the user's PostManager notifies of changes.
 */

public class ProfilePanel extends JPanel implements Observer {
    private MainFrame mainFrame;
    private UserManager userManager;
    private JLabel nameLabel;
    private JLabel usernameLabel;
    private JLabel followersLabel;
    private JLabel followingLabel;
    private JButton backButton;
    private JButton friendsButton;
    private JButton createGroupButton;
    private JButton myGroupsButton;
    private JTextArea postsArea;
    private User currentUser;

    public ProfilePanel(MainFrame mainFrame, UserManager userManager) {
        this.mainFrame = mainFrame;
        this.userManager = userManager;
        setLayout(new BorderLayout());

        
        setBackground(new Color(173, 216, 230)); // Light Blue

        nameLabel = createLabel();
        usernameLabel = createLabel();
        followersLabel = createLabel();
        followingLabel = createLabel();
        backButton = createButton("Back");
        friendsButton = createButton("Friends");
        createGroupButton = createButton("Create Group");
        myGroupsButton = createButton("My Groups");
        postsArea = new JTextArea();
        postsArea.setEditable(false);
        postsArea.setBackground(Color.WHITE);
        postsArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Dark blue border

        backButton.addActionListener(e -> mainFrame.switchTo("Home"));
        friendsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFriends();
            }
        });
        createGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createGroup();
            }
        });
        myGroupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGroups();
            }
        });

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(173, 216, 230)); // Light blue
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(createTextLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(nameLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(createTextLabel("Username:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(usernameLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(createTextLabel("Followers:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(followersLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(createTextLabel("Following:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(followingLabel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(173, 216, 230)); // Light blue
        buttonPanel.add(backButton);
        buttonPanel.add(friendsButton);
        buttonPanel.add(createGroupButton);
        buttonPanel.add(myGroupsButton);

        JScrollPane scrollPane = ScrollDecorator.decorate(infoPanel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(173, 216, 230)); // Light blue 
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(new JScrollPane(postsArea), BorderLayout.SOUTH);
        contentPanel.add(buttonPanel, BorderLayout.NORTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Displays the profile of the specified user.
     * Adds this panel as an observer to the user's PostManager.
     */
    public void displayProfile(User user) {
        currentUser = user;
        nameLabel.setText(user.getFullName());
        usernameLabel.setText(user.getUsername());
        followersLabel.setText(String.valueOf(user.getFollowManager().getFollowers().size()));
        followingLabel.setText(String.valueOf(user.getFollowManager().getFollowing().size()));
        user.getPostManager().addObserver(this);
        updatePosts();
    }
    
    
    
    /**
     * Called when the PostManager notifies observers 
     * Updates the profile to display the latest posts.
     */
    @Override
    public void update() {
        updatePosts();
    }

    private void updatePosts() {
        if (currentUser != null) {
            postsArea.setText("");
            List<Post> posts = currentUser.getPostManager().getPosts();
            for (Post post : posts) {
                postsArea.append(post.getContent() + " (" + post.getLikeCount() + " likes)\n");
            }
        }
    }

    private void showFriends() { //Displays a dialog with the user's friends.
        if (currentUser != null) {
            List<User> friends = currentUser.getFriendManager().getFriends();
            if (friends.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No friends to display.");
            } else {
                JDialog friendsDialog = new JDialog(mainFrame, "Friends of " + currentUser.getUsername(), true);
                friendsDialog.setSize(300, 400);
                friendsDialog.setLayout(new BorderLayout());

                DefaultListModel<String> friendsListModel = new DefaultListModel<>();
                for (User friend : friends) {
                    friendsListModel.addElement(friend.getUsername());
                }

                JList<String> friendsList = new JList<>(friendsListModel);
                friendsDialog.add(new JScrollPane(friendsList), BorderLayout.CENTER);

                JButton closeButton = createButton("Close");
                closeButton.addActionListener(e -> friendsDialog.dispose());
                friendsDialog.add(closeButton, BorderLayout.SOUTH);

                friendsDialog.setLocationRelativeTo(mainFrame);
                friendsDialog.setVisible(true);
            }
        }
    }

    private void createGroup() { // Displays a dialog for creating a new group.
        if (currentUser != null) {
            List<User> friends = currentUser.getFriendManager().getFriends();
            if (friends.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have no friends to add to a group.");
                return;
            }

            JDialog createGroupDialog = new JDialog(mainFrame, "Create Group", true);
            createGroupDialog.setSize(400, 300);
            createGroupDialog.setLayout(new BorderLayout());

            JTextField groupNameField = new JTextField(20);
            DefaultListModel<String> friendsListModel = new DefaultListModel<>();
            for (User friend : friends) {
                friendsListModel.addElement(friend.getUsername());
            }

            JList<String> friendsList = new JList<>(friendsListModel);
            friendsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            JButton createButton = createButton("Create");
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String groupName = groupNameField.getText();
                    if (groupName.isEmpty()) {
                        JOptionPane.showMessageDialog(createGroupDialog, "Group name cannot be empty.");
                        return;
                    }

                    Group newGroup = new Group(groupName);
                    List<String> selectedFriends = friendsList.getSelectedValuesList();
                    for (String friendUsername : selectedFriends) {
                        User friend = userManager.getUser(friendUsername);
                        if (friend != null) {
                            newGroup.addMember(friend);
                        }
                    }
                    newGroup.addMember(currentUser);
                    //currentUser.getGroupManager().addGroup(newGroup); // bunu iki defa eklememek için sildik
                    userManager.saveUser(currentUser);
                    
                    // Add the group to all members' GroupManager instances
                    currentUser.getGroupManager().addGroupToAllMembers(newGroup);
                    
                    createGroupDialog.dispose();
                }
            });

            createGroupDialog.add(new JLabel("Group Name:"), BorderLayout.NORTH);
            createGroupDialog.add(groupNameField, BorderLayout.CENTER);
            createGroupDialog.add(new JScrollPane(friendsList), BorderLayout.WEST);
            createGroupDialog.add(createButton, BorderLayout.SOUTH);

            createGroupDialog.setLocationRelativeTo(mainFrame);
            createGroupDialog.setVisible(true);
        }
    }

    private void showGroups() { // Displays a dialog with the users groups.
        if (currentUser != null) {
            List<Group> groups = currentUser.getGroupManager().getGroups();
            if (groups.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No groups to display.");
            } else {
                JDialog groupsDialog = new JDialog(mainFrame, "My Groups", true);
                groupsDialog.setSize(300, 400);
                groupsDialog.setLayout(new BorderLayout());

                DefaultListModel<String> groupsListModel = new DefaultListModel<>();
                for (Group group : groups) {
                    groupsListModel.addElement(group.getName());
                }

                JList<String> groupsList = new JList<>(groupsListModel);
                groupsList.addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        String selectedGroupName = groupsList.getSelectedValue();
                        Group selectedGroup = currentUser.getGroupManager().getGroupByName(selectedGroupName);
                        if (selectedGroup != null) {
                            displayGroupWall(selectedGroup);
                        }
                    }
                });

                groupsDialog.add(new JScrollPane(groupsList), BorderLayout.CENTER);

                JButton closeButton = createButton("Close");
                closeButton.addActionListener(e -> groupsDialog.dispose());
                groupsDialog.add(closeButton, BorderLayout.SOUTH);

                groupsDialog.setLocationRelativeTo(mainFrame);
                groupsDialog.setVisible(true);
            }
        }
    }

    private void displayGroupWall(Group group) { //Displays the wall of the specified group.
        JDialog groupWallDialog = new JDialog(mainFrame, "Group Wall - " + group.getName(), true);
        groupWallDialog.setSize(500, 400);
        groupWallDialog.setLayout(new BorderLayout());

        JTextArea groupPostsArea = new JTextArea();
        groupPostsArea.setEditable(false);
        groupPostsArea.setBackground(Color.WHITE);
        groupPostsArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Dark blue border
        List<Post> posts = group.getPostManager().getPosts();
        for (Post post : posts) {
            groupPostsArea.append(post.getUser().getFullName() + ": " + post.getContent() + " (" + post.getLikeCount() + " likes)\n");
        }

        JTextArea newPostArea = new JTextArea(3, 50);
        newPostArea.setBackground(Color.WHITE);
        newPostArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204))); // Dark blue border
        JButton postButton = createButton("Post");
        postButton.addActionListener(e -> {
            String content = newPostArea.getText();
            if (!content.isEmpty()) {
                Post newPost = new Post(content, currentUser);
                group.getPostManager().addPost(newPost);
                newPostArea.setText("");
                groupPostsArea.append(newPost.getUser().getFullName() + ": " + newPost.getContent() + " (" + newPost.getLikeCount() + " likes)\n");
                userManager.saveUser(currentUser);
            }
        });

        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.setBackground(new Color(173, 216, 230)); // Light blue background
        postPanel.add(new JScrollPane(newPostArea), BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.EAST);

        groupWallDialog.add(new JScrollPane(groupPostsArea), BorderLayout.CENTER);
        groupWallDialog.add(postPanel, BorderLayout.SOUTH);

        groupWallDialog.setLocationRelativeTo(mainFrame);
        groupWallDialog.setVisible(true);
    }

    private JLabel createLabel() {
        JLabel label = new JLabel();
        label.setForeground(new Color(0, 102, 204)); // Dark blue color for text
        return label;
    }

    private JLabel createTextLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0, 102, 204)); // Dark blue color for text
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 102, 204)); // Dark blue background
        button.setForeground(Color.WHITE);
        return button;
    }
}
