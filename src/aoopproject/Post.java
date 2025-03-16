package aoopproject;

import java.util.Date;
/**
 * Represents a post made by a user.
 * Supports saving and restoring its state using the Memento design pattern.
 */
public class Post {
    private String content;
    private User user;
    private int likeCount;
    private Date timestamp;

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
        this.likeCount = 0;
        this.timestamp = new Date();
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void incrementLikes() {
        likeCount++;
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    //Saves the current state of the post to a memento.
    public PostMemento saveToMemento() { 
        return new PostMemento(content, user, likeCount, timestamp);
    }

    // Restores the state of the post from a memento.
    public void restoreFromMemento(PostMemento memento) {
        this.content = memento.getContent();
        this.user = memento.getUser();
        this.likeCount = memento.getLikeCount();
        this.timestamp = memento.getTimestamp();
    }
}
