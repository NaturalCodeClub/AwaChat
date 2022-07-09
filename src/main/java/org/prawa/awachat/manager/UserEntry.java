package org.prawa.awachat.manager;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserEntry implements Serializable {
    private static final Gson gson = new Gson();
    private boolean isAdmin;
    private final List<String> friends;
    private final String name;
    private String password;

    public UserEntry(String userName,String password){
        this.name = userName;
        this.password = password;
        this.friends = new CopyOnWriteArrayList<>();
        this.isAdmin = false;
    }

    public void setAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public void setPassword(String newIn){
        this.password = newIn;
    }

    public void addFriend(String userIn){
        this.friends.add(userIn);
    }

    public void removeFriend(String userIn){
        this.friends.remove(userIn);
    }

    public List<String> getFriends(){
        return this.friends;
    }

    public String getUserName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public boolean isAdmin(){
        return this.isAdmin;
    }

    public String getJsonData(){
        return gson.toJson(this);
    }
}
