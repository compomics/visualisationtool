package com.compomics.tessa.dataTransferObjects;

/**
 * Created by demet on 12/4/2017.
 */
public class Peptide {

    private Integer start;

    private Integer end;

    private String sequence;

    private float percentage;

    private String colorCode;

    public Peptide(Integer start, Integer end, String sequence, float percentage, String colorCode) {
        this.start = start;
        this.end = end;
        this.sequence = sequence;
        this.percentage = percentage;
        this.colorCode = colorCode;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
