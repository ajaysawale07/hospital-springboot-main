package com.hospital.model;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bed_assignments")
public class BedAssignment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "bed_id", nullable = false, updatable = false)
    private int bedId;

    @Column(name = "patient_id", nullable = false, updatable = false)
    private int patientId;

    // Constructors
    public BedAssignment() {}

    public BedAssignment(int bedId, int patientId) {
        this.bedId = bedId;
        this.patientId = patientId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getBedId() {
        return bedId;
    }
    public void setBedId(int bedId) {
        this.bedId = bedId;
    }
    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    @Override
    public String toString() {
        return "BedAssignment{" +
                "id=" + id +
                ", bedId=" + bedId +
                ", patientId=" + patientId +
                '}';
    }
}
