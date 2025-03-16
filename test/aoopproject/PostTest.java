package aoopproject;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class PostTest {

    private Post post;
    private User umut;

    @Before
    public void setUp() {
        umut = new User("umut", "umut123", "Umut Ozturk", true);
        post = new Post("I Love AOOP Course <3", umut);
    }

    @Test
    public void testGetContent() {
        assertEquals("I Love AOOP Course <3", post.getContent());
    }

    @Test
    public void testGetUser() {
        assertEquals(umut, post.getUser());
    }

    @Test
    public void testGetLikeCount() {
        assertEquals(0, post.getLikeCount());
    }

    @Test
    public void testIncrementLikes() {
        post.incrementLikes();
        assertEquals(1, post.getLikeCount());
    }

    @Test
    public void testGetTimestamp() {
        Date timestamp = post.getTimestamp();
        assertNotNull(timestamp);
        assertTrue(timestamp.getTime() <= new Date().getTime());
    }

    @Test
    public void testSaveToMemento() {
        PostMemento memento = post.saveToMemento();
        assertEquals(post.getContent(), memento.getContent());
        assertEquals(post.getUser(), memento.getUser());
        assertEquals(post.getLikeCount(), memento.getLikeCount());
        assertEquals(post.getTimestamp(), memento.getTimestamp());
    }

    @Test
    public void testRestoreFromMemento() {
        PostMemento memento = post.saveToMemento();
        post.incrementLikes();
        post.restoreFromMemento(memento);
        assertEquals(0, post.getLikeCount());
    }
}
