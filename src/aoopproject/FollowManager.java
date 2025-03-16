package aoopproject;

import java.util.ArrayList;
import java.util.List;

public class FollowManager {
    private List<User> followers;
    private List<User> following;

    public FollowManager() {
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public void follow(User follower, User followee) {
        if (!following.contains(followee)) {
            following.add(followee);
            followee.getFollowManager().addFollower(follower);
            System.out.println(follower.getUsername() + " is now following " + followee.getUsername());
        }
    }

    public void addFollower(User follower) {
        if (!followers.contains(follower)) {
            followers.add(follower);
        }
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowing() {
        return following;
    }
}

