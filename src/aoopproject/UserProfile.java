
package aoopproject;

/**
 * Class to display user profile information.
 * Uses the UserAdapter to adapt the User object for display.
 */
public class UserProfile {
    private User user;

    public UserProfile(User user) {
        this.user = user;
    }

    public void displayProfile() { // Displays the profile information using the UserAdapter.
        // Create an adapter for the User object.
        UserAdapter adapter = new UserAdapter(user);
        // Display the adapted user data.
        System.out.println(adapter.getUserData());
        /* Without adapter design pattern
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Searchable: " + (user.isSearchable() ? "Yes" : "No"));
        System.out.println("Followers: " + user.getFollowManager().getFollowers().size());
        System.out.println("Following: " + user.getFollowManager().getFollowing().size());
        */
    }
}

