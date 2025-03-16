package aoopproject;

public class FriendRequest {
    private User sender;
    private User receiver;
    private boolean isAccepted;

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.isAccepted = false;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void accept() {
        isAccepted = true;
    }
}
