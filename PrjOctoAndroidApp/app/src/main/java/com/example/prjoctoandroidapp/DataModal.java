package com.example.prjoctoandroidapp;

public class DataModal {

    // variables for our
    // string and confidence.
    private String result;
    private float confidence;

    // constructor
    public DataModal(String result, float confidence) {
        this.result = result;
        this.confidence = confidence;
    }

    // getter and setter methods
    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}

