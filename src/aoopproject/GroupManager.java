package aoopproject;

import static aoopproject.UserManager.USERS_DIR;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupManager {
    private List<Group> groups;

    public GroupManager() {
        groups = new ArrayList<>();
    }
    

    public void addGroup(Group group) {
        groups.add(group);
    }
    


    public List<Group> getGroups() {
        return new ArrayList<>(groups);
    }

    public Group getGroupByName(String groupName) {
        for (Group group : groups) {
            if (group.getName().equals(groupName)) {
                return group;
            }
        }
        return null;
    }
    
    //bütün üyelere gerekli yüklemelerin yapılması için
    public void addGroupToAllMembers(Group group) {
        for (User member : group.getMembers()) {
            member.getGroupManager().addGroup(group);
        }
    }
  
}

