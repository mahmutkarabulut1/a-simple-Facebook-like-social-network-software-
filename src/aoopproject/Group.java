package aoopproject;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<User> members;
    private PostManager postManager;

    public Group(String name) {
        this.name = name;
        this.members = new ArrayList<>();
        this.postManager = new PostManager();
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return new ArrayList<>(members);
    }

    public void addMember(User member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }

    public PostManager getPostManager() {
        return postManager;
    }
}
