package com.compomics.tessa.dataTransferObjects;

/**
 * Created by demet on 12/4/2017.
 */
public class Protein {

    private String name;

    private String sequence;

    public Protein(String name, String sequence) {
        this.name = name;
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
