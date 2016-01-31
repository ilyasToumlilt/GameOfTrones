package com.toumlilt.gameoftrones;

/**
 * Classe Modèle pour stocker les informations d'une Sanisette.
 */
public class Sanitary {

    /**
     * Coordonnée latitude
     */
    private Double latitude;

    /**
     * Coordonnée longitude
     */
    private Double longitude;

    /**
     * Points de vie restants
     */
    private Integer remainingLife;

    /**
     * Points de vie par defaut
     */
    private final static Integer DEFAULT_REMAINING_LIFE = 10;

    /***********************************************************************************************
     * Constructors
     **********************************************************************************************/
    public Sanitary(Double latitude, Double longitude, Integer remainingLife){
        this.latitude = latitude;
        this.longitude = longitude;
        this.remainingLife= remainingLife;
    }

    public Sanitary(Double latitude, Double longitude){
        this(latitude, longitude, DEFAULT_REMAINING_LIFE);
    }

    /***********************************************************************************************
     * Getters + Setterss
     **********************************************************************************************/
    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getRemainingLife() {
        return remainingLife;
    }

    public void setRemainingLife(Integer remainingLife) {
        this.remainingLife = remainingLife;
    }
}
