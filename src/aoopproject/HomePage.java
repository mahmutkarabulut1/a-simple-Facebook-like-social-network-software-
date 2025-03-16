/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aoopproject;

/**
 *
 * @author mahmut
 */
import java.util.List;


public class HomePage {
    private User loggedInUser;
    private List<User> friends;

    public HomePage(User loggedInUser, List<User> friends) {
        this.loggedInUser = loggedInUser;
        this.friends = friends;
    }

    public void displayPosts() {
        for (User friend : friends) {
            for (Post post : friend.getPostManager().getPosts()) {
                System.out.println(friend.getFullName() + ": " + post.getContent() + " (" + post.getLikeCount() + " likes)");
            }
        }
    }

    public void likePost(User user, Post post) {
        user.getPostManager().likePost(post);
        System.out.println("Post liked: " + post.getContent());
    }
}


