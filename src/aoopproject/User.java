package aoopproject;

public class User {
    private String username;
    private String password;
    private String fullName;
    private boolean isSearchable;
    private PostManager postManager;
    private FriendManager friendManager;
    private FollowManager followManager;
    private GroupManager groupManager;

    public User(String username, String password, String fullName, boolean isSearchable) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.isSearchable = isSearchable;
        this.postManager = new PostManager();
        this.friendManager = new FriendManager(this); // Pass 'this' as owner
        this.followManager = new FollowManager();
        this.groupManager = new GroupManager();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isSearchable() {
        return isSearchable;
    }

    public PostManager getPostManager() {
        return postManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public FollowManager getFollowManager() {
        return followManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
