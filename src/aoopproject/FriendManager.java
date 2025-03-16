package aoopproject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    private User owner;
    private List<FriendRequest> sentFriendRequests;
    private List<FriendRequest> receivedFriendRequests;
    private List<User> friends;

    public FriendManager(User owner) {
        this.owner = owner;
        sentFriendRequests = new ArrayList<>();
        receivedFriendRequests = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<FriendRequest> getSentFriendRequests() {
        return new ArrayList<>(sentFriendRequests);
    }

    public List<FriendRequest> getReceivedFriendRequests() {
        return new ArrayList<>(receivedFriendRequests);
    }

    public void sendFriendRequest(User sender, User receiver) {
        FriendRequest request = new FriendRequest(sender, receiver);
        sentFriendRequests.add(request);
        receiver.getFriendManager().addReceivedFriendRequest(request);
    }

    public void addSentFriendRequest(FriendRequest request) {
        sentFriendRequests.add(request);
    }

    public void addReceivedFriendRequest(FriendRequest request) {
        receivedFriendRequests.add(request);
    }

    public void acceptFriendRequest(FriendRequest request, UserManager userManager) {
        User sender = request.getSender();
        User receiver = request.getReceiver();
        addFriend(sender);
        sender.getFriendManager().addFriend(receiver);
        receivedFriendRequests.remove(request);
        sender.getFriendManager().getSentFriendRequests().remove(request);
        
        // Save both users
        userManager.saveBothUsers(sender, receiver);
    }

    public void removeFriendRequest(FriendRequest request) {
        receivedFriendRequests.remove(request);
    }

    public void addFriend(User friend) {
        addFriend(friend, true);
    }

    public void addFriend(User friend, boolean saveToFile) {
        if (!friends.contains(friend)) {
            friends.add(friend);
            if (saveToFile) {
                saveFriendToFile(friend);
            }
        }
    }

    private void saveFriendToFile(User friend) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(UserManager.FRIENDS_FILE, true))) {
            writer.write("User:" + owner.getUsername());
            writer.newLine();
            writer.write(friend.getUsername());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFriendsFromFile() {
        // Yükleme işlemi UserManager tarafından yapılacak, bu metod boş bırakılabilir
    }
}
