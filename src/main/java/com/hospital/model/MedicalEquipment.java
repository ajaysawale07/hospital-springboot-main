package com.hospital.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "medical_equipment")
public class MedicalEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String model;
    private String manufacturer;
    private Date purchaseDate;
    private Date lastMaintenanceDate;
    private String location;
    private String status;
    private String serialNumber;
    private boolean requiresCertification;

    // Private constructor - objects should be created using the Builder
    private MedicalEquipment() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public Date getPurchaseDate() { return purchaseDate; }
    public Date getLastMaintenanceDate() { return lastMaintenanceDate; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getSerialNumber() { return serialNumber; }
    public boolean isRequiresCertification() { return requiresCertification; }

    // Builder class
    public static class Builder {
        private MedicalEquipment equipment;

        public Builder() {
            equipment = new MedicalEquipment();
        }

        public Builder name(String name) {
            equipment.name = name;
            return this;
        }

        public Builder model(String model) {
            equipment.model = model;
            return this;
        }

        public Builder manufacturer(String manufacturer) {
            equipment.manufacturer = manufacturer;
            return this;
        }

        public Builder purchaseDate(Date purchaseDate) {
            equipment.purchaseDate = purchaseDate;
            return this;
        }

        public Builder lastMaintenanceDate(Date lastMaintenanceDate) {
            equipment.lastMaintenanceDate = lastMaintenanceDate;
            return this;
        }

        public Builder location(String location) {
            equipment.location = location;
            return this;
        }

        public Builder status(String status) {
            equipment.status = status;
            return this;
        }

        public Builder serialNumber(String serialNumber) {
            equipment.serialNumber = serialNumber;
            return this;
        }

        public Builder requiresCertification(boolean requiresCertification) {
            equipment.requiresCertification = requiresCertification;
            return this;
        }

        public MedicalEquipment build() {
            // Validate required fields
            if (equipment.name == null || equipment.manufacturer == null) {
                throw new IllegalStateException("Name and manufacturer are required fields");
            }
            return equipment;
        }
    }
}