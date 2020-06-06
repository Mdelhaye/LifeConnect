package com.example.lifeconnect.model;

import java.util.ArrayList;

public class Users {

    private ArrayList<User> users;

    public Users(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Users -> {" +
                "users = " + users +
                '}';
    }
}
