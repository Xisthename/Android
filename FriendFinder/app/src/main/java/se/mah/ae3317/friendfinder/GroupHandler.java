package se.mah.ae3317.friendfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xisth on 2016-10-08.
 */
public class GroupHandler {
    private List<Group> groups;
    private List<String> register;
    public static String name;

    public GroupHandler() {
        groups = new ArrayList<>();
        register = new ArrayList<>();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<String> getRegisters() {
        return register;
    }

    public String getName() {
        return name;
    }

    public void updateGroups(JSONObject object) throws JSONException {
        groups.clear();

        JSONArray groupsArray = object.getJSONArray("groups");
        for (int i = 0; i < groupsArray.length(); i++) {
            JSONObject singleGroup = groupsArray.getJSONObject(i);

            String groupName = singleGroup.getString("group");
            groups.add(new Group(groupName));
            Log.d("Group added to groups:", groupName);
        }
    }

    public void addGroup(String groupName) {
        groups.add(new Group(groupName));
    }

    public Group getGroup(String name) {
        Group returnGroup = null;
        for (Group group: groups) {
            if (group.getName().equals(name)) {
                returnGroup = group;
            }
        }
        return returnGroup;
    }

    public void updateMembers(JSONObject object) throws JSONException {
        Group group = getGroup(object.getString("group"));

        if (group == null) {
            return;
        }
        group.clearUsers();
        JSONArray membersArray = object.getJSONArray("members");
        for (int i = 0; i < membersArray.length(); i++) {
            JSONObject singleMember = membersArray.getJSONObject(i);
            String member = singleMember.getString("member");
            group.addUser(member);

            if (name.equals(member)) {
                group.setSubscribe(true);
            }
            Log.d("Member added to " + group.getName(), singleMember.getString("member"));
        }
    }

    public void updateRegister(JSONObject object) throws JSONException {
        register.add(object.getString("id"));
        Log.d("ADDED: ", object.getString("id"));
    }
}
