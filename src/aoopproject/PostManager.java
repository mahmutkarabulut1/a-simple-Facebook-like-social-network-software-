package aoopproject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages posts for a user or group.
 * Extends Observable to notify observers of changes to the post list.
 * Supports saving and restoring post history using the Memento design pattern.
 */
public class PostManager extends Observable {
    private List<Post> posts;
    private List<PostMemento> postHistory; // List of mementos for posts

    public PostManager() {
        posts = new ArrayList<>();
        postHistory = new ArrayList<>();
    }

    public void addPost(Post post) { //Adds a post and notifies observers of the change
        posts.add(post);
        postHistory.add(post.saveToMemento());
        notifyObservers();
    }

    public List<Post> getPosts() { 
        return new ArrayList<>(posts);
    }

    public List<PostMemento> getPostHistory() { //Gets a list of post mementos.
        return new ArrayList<>(postHistory);
    }

    public void likePost(Post post) { // Likes a post and notifies observers of the change.
        post.incrementLikes();
        postHistory.add(post.saveToMemento());
        notifyObservers();
    }

    //  Gets a list of posts sorted by timestamp in descending order.
    public List<Post> getSortedPosts() { 
        List<Post> sortedPosts = new ArrayList<>(posts);
        sortedPosts.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return sortedPosts;
    }
}
