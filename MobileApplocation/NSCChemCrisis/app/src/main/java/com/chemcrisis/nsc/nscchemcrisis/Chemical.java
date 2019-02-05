package com.chemcrisis.nsc.nscchemcrisis;

public class Chemical {
    private String chemicalName, chemicalThaiName,effectsOfChemical,molecularWeight ,violenceOfChemical;

    public Chemical(String chemicalName, String chemicalThaiName, String effectsOfChemical,String molecularWeight, String violenceOfChemical) {
        this.chemicalName = chemicalName;
        this.chemicalThaiName = chemicalThaiName;
        this.effectsOfChemical = effectsOfChemical;
        this.violenceOfChemical = violenceOfChemical;
        this.molecularWeight = molecularWeight;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getChemicalThaiName() {
        return chemicalThaiName;
    }

    public void setChemicalThaiName(String chemicalThaiName) {
        this.chemicalThaiName = chemicalThaiName;
    }

    public String getEffectsOfChemical() {
        return effectsOfChemical;
    }

    public void setEffectsOfChemical(String effectsOfChemical) {
        this.effectsOfChemical = effectsOfChemical;
    }

    public String getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(String molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public String getViolenceOfChemical() {
        return violenceOfChemical;
    }

    public void setViolenceOfChemical(String violenceOfChemical) {
        this.violenceOfChemical = violenceOfChemical;
    }
}
