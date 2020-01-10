package com.company;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String name;
    private List<User> users;
    private User user;

    public Group(String name, ArrayList<User> users) {
        this.name = name;
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
        System.out.println(user.getUserName() + " was added to group");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(String userName) {

    }

    public void removeUser(Group group, User user) {

    }
}
