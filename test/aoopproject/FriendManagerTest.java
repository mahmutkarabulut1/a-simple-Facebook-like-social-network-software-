package aoopproject;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class FriendManagerTest {

    private FriendManager friendManager;
    private User bengi;
    private User mahmut;
    private UserManager userManager;

    @Before
    public void setUp() {
        bengi = new User("bengi", "bengi123", "Bengi İlhan", true);
        mahmut = new User("mahmut", "mahmut123", "Mahmut Karabulut", true);
        friendManager = new FriendManager(bengi);
        userManager = UserManager.getInstance();
        userManager.addUser(bengi);
        userManager.addUser(mahmut);
    }

    @Test
    public void testSendFriendRequest() {
        friendManager.sendFriendRequest(bengi, mahmut);
        List<FriendRequest> sentRequests = friendManager.getSentFriendRequests();
        List<FriendRequest> receivedRequests = mahmut.getFriendManager().getReceivedFriendRequests();
        
        assertEquals(1, sentRequests.size());
        assertEquals(1, receivedRequests.size());
        assertEquals(mahmut, sentRequests.get(0).getReceiver());
        assertEquals(bengi, receivedRequests.get(0).getSender());
    }

    @Test
    public void testAcceptFriendRequest() {
        FriendRequest request = new FriendRequest(bengi, mahmut);
        mahmut.getFriendManager().addReceivedFriendRequest(request);
        bengi.getFriendManager().addSentFriendRequest(request);

        mahmut.getFriendManager().acceptFriendRequest(request, userManager);
        List<User> friendsUser1 = bengi.getFriendManager().getFriends();
        List<User> friendsUser2 = mahmut.getFriendManager().getFriends();

        assertTrue(friendsUser1.contains(mahmut));
        assertTrue(friendsUser2.contains(bengi));
        assertFalse(mahmut.getFriendManager().getReceivedFriendRequests().contains(request));
        assert(bengi.getFriendManager().getSentFriendRequests().contains(request));
    }

    @Test
    public void testAddFriend() {
        friendManager.addFriend(mahmut);
        List<User> friends = friendManager.getFriends();
        
        assertEquals(1, friends.size());
        assertEquals("mahmut", friends.get(0).getUsername());
    }

    @Test
    public void testRemoveFriendRequest() {
        FriendRequest request = new FriendRequest(bengi, mahmut);
        mahmut.getFriendManager().addReceivedFriendRequest(request);

        mahmut.getFriendManager().removeFriendRequest(request);
        List<FriendRequest> receivedRequests = mahmut.getFriendManager().getReceivedFriendRequests();

        assertFalse(receivedRequests.contains(request));
    }

    @Test
    public void testGetFriends() {
        friendManager.addFriend(mahmut);
        List<User> friends = friendManager.getFriends();

        assertEquals(1, friends.size());
        assertEquals("mahmut", friends.get(0).getUsername());
    }

    @Test
    public void testGetSentFriendRequests() {
        FriendRequest request = new FriendRequest(bengi, mahmut);
        friendManager.addSentFriendRequest(request);

        List<FriendRequest> sentRequests = friendManager.getSentFriendRequests();
        assertEquals(1, sentRequests.size());
        assertEquals(mahmut, sentRequests.get(0).getReceiver());
    }

    @Test
    public void testGetReceivedFriendRequests() {
        FriendRequest request = new FriendRequest(bengi, mahmut);
        mahmut.getFriendManager().addReceivedFriendRequest(request);

        List<FriendRequest> receivedRequests = mahmut.getFriendManager().getReceivedFriendRequests();
        assertEquals(1, receivedRequests.size());
        assertEquals(bengi, receivedRequests.get(0).getSender());
    }
}
