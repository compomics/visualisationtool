package com.compomics.tessa.dataTransferObjects;

import java.util.List;

/**
 * Created by demet on 12/4/2017.
 */
public class ProteinDTO {

    Protein protein;

    List<Peptide> peptides;

    public ProteinDTO(Protein protein, List<Peptide> peptides) {
        this.protein = protein;
        this.peptides = peptides;
    }

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    public List<Peptide> getPeptides() {
        return peptides;
    }

    public void setPeptides(List<Peptide> peptides) {
        this.peptides = peptides;
    }

}
