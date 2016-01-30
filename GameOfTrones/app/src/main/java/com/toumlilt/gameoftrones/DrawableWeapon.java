package com.toumlilt.gameoftrones;

public class DrawableWeapon extends Weapon {
    private int image;

    public DrawableWeapon(String name, Integer pv, Integer scope, int image) {
        super(name, pv, scope);
        this.image = image;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
