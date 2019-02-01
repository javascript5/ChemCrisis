package com.chemcrisis.nsc.nscchemcrisis.Gaussian;

public class Gaussian {
    private Double latitude;
    private Double longtitude;
    private Double weight;

    public Gaussian(Double latitude, Double longtitude, Double weight) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.weight = weight;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
