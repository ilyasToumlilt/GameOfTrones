package com.toumlilt.gameoftrones;

/**
 * Created by waye on 29/01/2016.
 */
public class Player {

    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = (username.isEmpty()) ? "NoName" : username;
    }

    public Player(String username) {
        this.setUsername(username);
    }
}
