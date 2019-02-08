package com.chemcrisis.nsc.nscchemcrisis;

public class Chemical {
    private String chemicalName, chemicalThaiName,effectsOfChemical,molecularWeight ,violenceOfChemical;
    public Chemical(){

    }
    public Chemical(String chemicalName, String chemicalThaiName, String effectsOfChemical, String molecularWeight, String violenceOfChemical) {
        this.chemicalName = chemicalName;
        this.chemicalThaiName = chemicalThaiName;
        this.effectsOfChemical = effectsOfChemical;
        this.molecularWeight = molecularWeight;
        this.violenceOfChemical = violenceOfChemical;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public String getChemicalThaiName() {
        return chemicalThaiName;
    }

    public String getEffectsOfChemical() {
        return effectsOfChemical;
    }

    public String getMolecularWeight() {
        return molecularWeight;
    }

    public String getViolenceOfChemical() {
        return violenceOfChemical;
    }
}
