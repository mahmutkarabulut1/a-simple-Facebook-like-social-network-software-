package aoopproject;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    private User ece;
    private User umut;

    @Before
    public void setUp() {
        ece = new User("ece", "ece123", "Ece Menemencioglu", true);
        umut = new User("umut","umut123","Umut Ozturk", false);
    }

    @Test
    public void testGetUsername() {
        assertEquals("ece", ece.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("ece123", ece.getPassword());
    }

    @Test
    public void testGetFullName() {
        assertEquals("Ece Menemencioglu", ece.getFullName());
    }

    @Test
    public void testIsSearchable() {
        assertTrue(ece.isSearchable());
        assertFalse(umut.isSearchable());
    }

    @Test
    public void testGetPostManager() {
        assertNotNull(ece.getPostManager());
    }

    @Test
    public void testGetFriendManager() {
        assertNotNull(ece.getFriendManager());
    }

    @Test
    public void testGetFollowManager() {
        assertNotNull(ece.getFollowManager());
    }

    @Test
    public void testGetGroupManager() {
        assertNotNull(ece.getGroupManager());
    }

    @Test
    public void testPostManagerFunctionality() {
        PostManager postManager = ece.getPostManager();
        Post post = new Post("Hello World", ece);
        postManager.addPost(post);

        assertEquals(1, postManager.getPosts().size());
        assertEquals("Hello World", postManager.getPosts().get(0).getContent());
    }

    @Test
    public void testFriendManagerFunctionality() {
        FriendManager friendManager = ece.getFriendManager();
        User friend = new User("randomUser", "password", "Friend", true);
        friendManager.addFriend(friend);

        assertEquals(1, friendManager.getFriends().size());
        assertEquals("randomUser", friendManager.getFriends().get(0).getUsername());
    }

    @Test
    public void testFollowManagerFunctionality() {
        FollowManager followManager = ece.getFollowManager();
        User followee = new User("follower1", "password", "Follower One", true);
        followManager.follow(ece, followee);

        assertEquals(1, followManager.getFollowing().size());
        assertEquals("follower1", followManager.getFollowing().get(0).getUsername());
        assertTrue(followee.getFollowManager().getFollowers().contains(ece));
    }

    @Test
    public void testGroupManagerFunctionality() {
        GroupManager groupManager = ece.getGroupManager();
        Group group = new Group("Group One");
        groupManager.addGroup(group);

        assertEquals(1, groupManager.getGroups().size());
        assertEquals("Group One", groupManager.getGroups().get(0).getName());
    }
}
