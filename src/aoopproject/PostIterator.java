
package aoopproject;


import java.util.Iterator;
import java.util.List;

public class PostIterator implements Iterator<Post> {
    private List<Post> posts;
    private int position; // Current position in the iteration
    
    public PostIterator(List<Post> posts) {
        this.posts = posts;
    }

    
    @Override 
    public boolean hasNext() {
        return position < posts.size();
    }

    @Override 
    public Post next() {
        return posts.get(position++);
    }
}

