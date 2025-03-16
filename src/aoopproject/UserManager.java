package aoopproject;

import java.io.*;
import java.util.*;

/**
 * Manages users in the social media application.
 * Implements the Singleton design pattern to ensure only one instance of UserManager exists.
 */
public class UserManager {
    private Map<String, User> users; // Map of username to user object
    private User loggedInUser;
    public static final String USERS_DIR = "users/"; // Users data path
    public static final String FRIENDS_FILE = USERS_DIR + "friends.txt"; // Friends data path

    // Singleton instance
    private static UserManager instance;

    // Private constructor to prevent instantiation
    private UserManager() {
        users = new HashMap<>();
        loadAllUsers(); // Önce kullanıcıları yükle
        loadAllFriends(); // Daha sonra arkadaşları yükle
    }

    // Provides access to the singleton instance of UserManager
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    //  Adds a user to the user manager and saves the user data.
    public void addUser(User user) {
        users.put(user.getUsername(), user);
        saveUser(user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public boolean validateUser(String username, String password) {
        User user = getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUser = user;
            return true;
        }
        return false;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public List<User> getSearchableUsers() {
        List<User> searchableUsers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isSearchable()) {
                searchableUsers.add(user);
            }
        }
        return searchableUsers;
    }

    private void loadAllUsers() {
        File usersDir = new File(USERS_DIR);
        if (!usersDir.exists()) {
            usersDir.mkdirs();
        }

        File[] userFiles = usersDir.listFiles((dir, name) -> name.endsWith(".txt") && !name.endsWith("_friends.txt"));
        if (userFiles != null) {
            for (File userFile : userFiles) {
                loadUser(userFile);
            }
        }
    }

    private void loadAllFriends() {
        File friendsFile = new File(FRIENDS_FILE);
        if (!friendsFile.exists()) {
            try {
                friendsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(friendsFile))) {
            String line;
            User currentUser = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("User:")) {
                    String username = line.substring(5).trim();
                    currentUser = getUser(username);
                } else if (currentUser != null) {
                    User friend = getUser(line.trim());
                    if (friend != null) {
                        currentUser.getFriendManager().addFriend(friend, false); // Arkadaş eklerken kaydetme işlemi yapılmıyor
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUser(File userFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String username = parts[0];
                    String password = parts[1];
                    String fullName = parts[2];
                    boolean isSearchable = Boolean.parseBoolean(parts[3]);
                    User user = new User(username, password, fullName, isSearchable);
                    users.put(username, user);

                    while ((line = reader.readLine()) != null) {
                        if (line.equals("POST")) {
                            String postContent = reader.readLine();
                            String timestampString = reader.readLine();
                            if (postContent == null || timestampString == null || timestampString.isEmpty()) {
                                continue;
                            }
                            long timestamp = Long.parseLong(timestampString);
                            //long timestamp = Long.parseLong(reader.readLine());
                            Post post = new Post(postContent, user);
                            user.getPostManager().addPost(post);
                        } else if (line.equals("FRIEND_REQUEST_SENT")) {
                            String receiverUsername = reader.readLine();
                            User receiver = getUser(receiverUsername);
                            if (receiver != null) {
                                user.getFriendManager().addSentFriendRequest(new FriendRequest(user, receiver));
                            }
                        } else if (line.equals("FRIEND_REQUEST_RECEIVED")) {
                            String senderUsername = reader.readLine();
                            User sender = getUser(senderUsername);
                            if (sender != null) {
                                user.getFriendManager().addReceivedFriendRequest(new FriendRequest(sender, user));
                            }
                        } else if (line.equals("FOLLOW")) {
                            String followeeUsername = reader.readLine();
                            User followee = getUser(followeeUsername);
                            if (followee != null) {
                                user.getFollowManager().follow(user, followee);
                            }
                        } else if (line.equals("GROUP")) {
                            String groupName = reader.readLine();
                            Group group = new Group(groupName);
                            int memberCount = Integer.parseInt(reader.readLine());
                            for (int i = 0; i < memberCount; i++) {
                                String memberUsername = reader.readLine();
                                User member = getUser(memberUsername);
                                if (member != null) {
                                    group.addMember(member);
                                }
                            }
                            int postCount = Integer.parseInt(reader.readLine());
                            for (int i = 0; postCount > 0 && i < postCount; i++) {
                                String postContent = reader.readLine();
                                long timestamp = Long.parseLong(reader.readLine());
                                Post post = new Post(postContent, user);
                                group.getPostManager().addPost(post);
                            }
                            user.getGroupManager().addGroup(group);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading user from file: " + userFile.getName());
        }
    }

    public void saveUser(User user) {
        File userFile = new File(USERS_DIR + user.getUsername() + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getFullName() + "," + user.isSearchable());
            writer.newLine();
            for (Post post : user.getPostManager().getPosts()) {
                writer.write("POST");
                writer.newLine();
                writer.write(post.getContent());
                writer.newLine();
                writer.write(String.valueOf(post.getTimestamp().getTime()));
                writer.newLine();
            }
            for (FriendRequest request : user.getFriendManager().getSentFriendRequests()) {
                writer.write("FRIEND_REQUEST_SENT");
                writer.newLine();
                writer.write(request.getReceiver().getUsername());
                writer.newLine();
            }
            for (FriendRequest request : user.getFriendManager().getReceivedFriendRequests()) {
                writer.write("FRIEND_REQUEST_RECEIVED");
                writer.newLine();
                writer.write(request.getSender().getUsername());
                writer.newLine();
            }
            for (User followee : user.getFollowManager().getFollowing()) {
                writer.write("FOLLOW");
                writer.newLine();
                writer.write(followee.getUsername());
                writer.newLine();
            }
            for (Group group : user.getGroupManager().getGroups()) {
                writer.write("GROUP");
                writer.newLine();
                writer.write(group.getName());
                writer.newLine();
                writer.write(String.valueOf(group.getMembers().size()));
                writer.newLine();
                for (User member : group.getMembers()) {
                    writer.write(member.getUsername());
                    writer.newLine();
                }
                List<Post> groupPosts = group.getPostManager().getPosts();
                writer.write(String.valueOf(groupPosts.size()));
                writer.newLine();
                for (Post post : groupPosts) {
                    writer.write(post.getContent());
                    writer.newLine();
                    writer.write(String.valueOf(post.getTimestamp().getTime()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // New helper method to save both users
    public void saveBothUsers(User user1, User user2) {
        saveUser(user1);
        saveUser(user2);
    }

    // New method to load a user by username
    public User loadUserByUsername(String username) {
        if (users.containsKey(username)) {
            return users.get(username);
        }

        File userFile = new File(USERS_DIR + username + ".txt");
        if (userFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String password = parts[1];
                        String fullName = parts[2];
                        boolean isSearchable = Boolean.parseBoolean(parts[3]);
                        User user = new User(username, password, fullName, isSearchable);
                        users.put(username, user);

                        while ((line = reader.readLine()) != null) {
                            if (line.equals("POST")) {
                                String postContent = reader.readLine();
                                long timestamp = Long.parseLong(reader.readLine());
                                Post post = new Post(postContent, user);
                                user.getPostManager().addPost(post);
                            } else if (line.equals("FRIEND")) {
                                String friendUsername = reader.readLine();
                                User friend = getUser(friendUsername);
                                if (friend != null) {
                                    user.getFriendManager().addFriend(friend, false);
                                }
                            } else if (line.equals("FRIEND_REQUEST_SENT")) {
                                String receiverUsername = reader.readLine();
                                User receiver = getUser(receiverUsername);
                                if (receiver != null) {
                                    user.getFriendManager().addSentFriendRequest(new FriendRequest(user, receiver));
                                }
                            } else if (line.equals("FRIEND_REQUEST_RECEIVED")) {
                                String senderUsername = reader.readLine();
                                User sender = getUser(senderUsername);
                                if (sender != null) {
                                    user.getFriendManager().addReceivedFriendRequest(new FriendRequest(sender, user));
                                }
                            } else if (line.equals("FOLLOW")) {
                                String followeeUsername = reader.readLine();
                                User followee = getUser(followeeUsername);
                                if (followee != null) {
                                    user.getFollowManager().follow(user, followee);
                                }
                            } else if (line.equals("GROUP")) {
                                String groupName = reader.readLine();
                                Group group = new Group(groupName);
                                int memberCount = Integer.parseInt(reader.readLine());
                                for (int i = 0; i < memberCount; i++) {
                                    String memberUsername = reader.readLine();
                                    User member = getUser(memberUsername);
                                    if (member != null) {
                                        group.addMember(member);
                                    }
                                }
                                int postCount = Integer.parseInt(reader.readLine());
                                for (int i = 0; postCount > 0 && i < postCount; i++) {
                                    String postContent = reader.readLine();
                                    long timestamp = Long.parseLong(reader.readLine());
                                    Post post = new Post(postContent, user);
                                    group.getPostManager().addPost(post);
                                }
                                user.getGroupManager().addGroup(group);
                            }
                        }
                        return user;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading user from file: " + userFile.getName());
            }
        }
        return null;
    }

    public void saveAllUsers() {
        for (User user : users.values()) {
            saveUser(user);
        }
        saveAllFriends(); // Tüm arkadaşlık ilişkilerini kaydet
    }

    private void saveAllFriends() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FRIENDS_FILE))) {
            for (User user : users.values()) {
                writer.write("User:" + user.getUsername());
                writer.newLine();
                for (User friend : user.getFriendManager().getFriends()) {
                    writer.write(friend.getUsername());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
