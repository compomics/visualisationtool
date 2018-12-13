package com.compomics.tessa.dataTransferObjects;

/**
 * Created by demet on 1/10/2018.
 */
public class VisualisationObject {

    int id;

    String proteinName;

    String regionName;

    ProteinDTO proteinGTG;

    ProteinDTO proteinJL;

    public VisualisationObject(int id, String proteinName, String regionName, ProteinDTO proteinGTG, ProteinDTO proteinJL) {
        this.id = id;
        this.proteinName = proteinName;
        this.regionName = regionName;
        this.proteinGTG = proteinGTG;
        this.proteinJL = proteinJL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public ProteinDTO getProteinGTG() {
        return proteinGTG;
    }

    public void setProteinGTG(ProteinDTO proteinGTG) {
        this.proteinGTG = proteinGTG;
    }

    public ProteinDTO getProteinJL() {
        return proteinJL;
    }

    public void setProteinJL(ProteinDTO proteinJL) {
        this.proteinJL = proteinJL;
    }
}
