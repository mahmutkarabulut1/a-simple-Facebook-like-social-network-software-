package aoopproject;

import java.util.Date;
/**
 * Memento class to store the state of a Post object.
 * This class is used to save and restore the state of a Post.
 */
public class PostMemento {
    private final String content;
    private final User user;
    private final int likeCount;
    private final Date timestamp;

    public PostMemento(String content, User user, int likeCount, Date timestamp) {
        this.content = content;
        this.user = user;
        this.likeCount = likeCount;
        this.timestamp = new Date(timestamp.getTime()); // Immutable date
    }

    // Getters to retrieve the state of the Post object
    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime()); // Immutable date
    }
}
