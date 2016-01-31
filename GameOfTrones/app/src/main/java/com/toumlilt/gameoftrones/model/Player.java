package com.toumlilt.gameoftrones.model;

/**
 * Classe Modèle pout stocker les informations d'un joueur.
 */
public class Player {

    /**
     * Pseudo du joueur
     */
    private String username;

    /**
     * Statut du joueur
     */
    private String userdesc;

    /***********************************************************************************************
     * Getters + Setters + Contructor
     **********************************************************************************************/
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
