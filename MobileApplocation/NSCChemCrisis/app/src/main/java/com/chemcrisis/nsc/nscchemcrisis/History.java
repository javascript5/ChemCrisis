package com.chemcrisis.nsc.nscchemcrisis;

public class History {
    private String accidentChemical, dateTime, factoryId;
    private double massEmissionRate, effectiveStackHeight;

    public History() {
    }

    public History(String accidentChemical, String dateTime, String factoryId, double massEmissionRate, double effectiveStackHeight) {
        this.accidentChemical = accidentChemical;
        this.dateTime = dateTime;
        this.factoryId = factoryId;
        this.massEmissionRate = massEmissionRate;
        this.effectiveStackHeight = effectiveStackHeight;
    }

    public String getAccidentChemical() {
        return accidentChemical;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public double getMassEmissionRate() {
        return massEmissionRate;
    }

    public double getEffectiveStackHeight() {
        return effectiveStackHeight;
    }
}
