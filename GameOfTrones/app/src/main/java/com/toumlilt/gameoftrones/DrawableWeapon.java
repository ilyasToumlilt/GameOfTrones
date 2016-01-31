package com.toumlilt.gameoftrones;

/**
 * Classe Modèle pour enrichir un Weapon d'une image et l'afficher dans une ListView par exemple.
 */
public class DrawableWeapon extends Weapon {
    /**
     * Id d'un drawable.
     * */
    private int image;

    /***********************************************************************************************
     * Constructors
     **********************************************************************************************/
    public DrawableWeapon(String name, Integer pv, Integer scope, int image) {
        super(name, pv, scope);
        this.image = image;
    }

    /***********************************************************************************************
     * Getters + Setterss
     **********************************************************************************************/
    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
