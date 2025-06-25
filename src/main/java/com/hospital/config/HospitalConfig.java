package com.hospital.config;

public class HospitalConfig {
    private static HospitalConfig instance;
    private String hospitalName;
    private int maxBeds;
    private int maxDoctors;

    private HospitalConfig() {
        // Default values
        this.hospitalName = "General Hospital";
        this.maxBeds = 100;
        this.maxDoctors = 50;
    }

    public static synchronized HospitalConfig getInstance() {
        if (instance == null) {
            instance = new HospitalConfig();
        }
        return instance;
    }

    // Getters and Setters
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getMaxBeds() {
        return maxBeds;
    }

    public void setMaxBeds(int maxBeds) {
        this.maxBeds = maxBeds;
    }

    public int getMaxDoctors() {
        return maxDoctors;
    }

    public void setMaxDoctors(int maxDoctors) {
        this.maxDoctors = maxDoctors;
    }
}