package com.toumlilt.gameoftrones;

public class Sanitary {
    private Double latitude;
    private Double longitude;
    private Integer remainingLife;

    private final static Integer DEFAULT_REMAINING_LIFE = 10;

    public Sanitary(Double latitude, Double longitude, Integer remainingLife){
        this.latitude = latitude;
        this.longitude = longitude;
        this.remainingLife= remainingLife;
    }

    public Sanitary(Double latitude, Double longitude){
        this(latitude, longitude, DEFAULT_REMAINING_LIFE);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Boolean isTaken() {
        return remainingLife == 0;
    }

    public Integer getRemainingLife() {
        return remainingLife;
    }

    public void setRemainingLife(Integer remainingLife) {
        this.remainingLife = remainingLife;
    }
}
