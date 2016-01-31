package com.toumlilt.gameoftrones.model;

import java.io.Serializable;

/**
 * Classe Modèle pout stocker les informations d'une arme.
 */
public class Weapon implements Serializable {

    /**
     * Nom de l'arme
     */
    private String name;

    /**
     * Dégats de l'arme
     */
    private Integer pv;

    /**
     * Portée d'attaque de l'arme
     */
    private Integer scope;


    /***********************************************************************************************
     * Constructor
     **********************************************************************************************/
    public Weapon(String name, Integer pv, Integer scope) {
        this.name = name;
        this.pv = pv;
        this.scope = scope;
    }

    /***********************************************************************************************
     * Getters + Setters
     **********************************************************************************************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }
}
