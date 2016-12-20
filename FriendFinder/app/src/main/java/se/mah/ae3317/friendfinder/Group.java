package se.mah.ae3317.friendfinder;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by xisth on 2016-10-06.
 */
public class Group {
    private String name;
    private boolean subscribe;
    private List<String> users;

    public Group(String name) {
        this.name = name;
        this.subscribe = false;
        users = new ArrayList<String>();
    }

    public void clearUsers() {
        users.clear();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public String getName() {
        return name;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public boolean isSubscribing() {
        return subscribe;
    }

    public int size() {
        return users.size();
    }
}