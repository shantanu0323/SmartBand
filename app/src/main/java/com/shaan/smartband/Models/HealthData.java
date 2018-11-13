package com.shaan.smartband.Models;

public class HealthData {

    private String createdAt;
    private String entryId;
    private String bodyTemp;
    private String atmosTemp;
    private String pulseRate;
    private String heatIndex;

    public HealthData(String createdAt, String entryId, String bodyTemp, String atmosTemp, String pulseRate, String heatIndex) {
        this.createdAt = createdAt;
        this.entryId = entryId;
        this.bodyTemp = bodyTemp;
        this.atmosTemp = atmosTemp;
        this.pulseRate = pulseRate;
        this.heatIndex = heatIndex;
    }

    public HealthData() {
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(String bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getAtmosTemp() {
        return atmosTemp;
    }

    public void setAtmosTemp(String atmosTemp) {
        this.atmosTemp = atmosTemp;
    }

    public String getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(String pulseRate) {
        this.pulseRate = pulseRate;
    }

    public String getHeatIndex() {
        return heatIndex;
    }

    public void setHeatIndex(String heatIndex) {
        this.heatIndex = heatIndex;
    }

    public String display() {
        String message = "|| createdAt = " + createdAt + " ; " +
                "entryId = " + entryId + " ; " +
                "bodyTemp = " + bodyTemp + " ; " +
                "atmosTemp = " + atmosTemp + " ; " +
                "pulseRate = " + pulseRate + " ; " +
                "heatIndex = " + heatIndex + " || ";
        return message;
    }
}

/*

Field 1 - Body Temperature Range - 0 to 30 degree
Field 2 - Atmosperic Temperature range 20 to 50
Field 3 - Pulse 0 to 90
Field 4 - Heat index 25 to 80

*/