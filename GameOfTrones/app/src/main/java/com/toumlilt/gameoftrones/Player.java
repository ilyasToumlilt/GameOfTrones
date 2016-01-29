package com.toumlilt.gameoftrones;

/**
 * Created by waye on 29/01/2016.
 */
public class Player {

    private String username;
    private String userdesc;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = (username.isEmpty()) ? "NoName" : username;
    }

    public String getUserdesc() {
        return this.userdesc;
    }

    public void setUserdesc(String userdesc) {
        this.userdesc = (userdesc.isEmpty()) ? "No Description" : userdesc;
    }

    public Player(String username, String userdesc) {
        this.setUsername(username);
        this.setUserdesc(userdesc);
    }
}
