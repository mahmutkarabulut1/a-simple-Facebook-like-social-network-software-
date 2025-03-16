
package aoopproject;

/**
 * Adapter class to adapt the User class to a different interface.
 * This class provides a simplified view of the User data.
 */
public class UserAdapter {
    private User user;

    public UserAdapter(User user) { //Constructor to initialize the adapter with a User object
        this.user = user;
    }

    /**
     * Gets the adapted user data as a formatted string.
     * This method adapts the User object to a new interface.
     */
    public String getUserData() {
        return "Username: " + user.getUsername() + "\nFull Name: " + user.getFullName() + "\nFollowers: " + user.getFollowManager().getFollowers().size() + "\nFollowing: " + user.getFollowManager().getFollowing().size();
    }
}

