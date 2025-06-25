package com.hospital.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "admission_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date admissionDate;

    @Column(name = "discharge_date")
    @Temporal(TemporalType.DATE)
    private Date dischargeDate;

    @Column(name = "treatment_details", columnDefinition = "TEXT")
    private String treatmentDetails;

    // Constructors
    public MedicalRecord() {}

    public MedicalRecord(Patient patient, Date admissionDate, Date dischargeDate, String treatmentDetails) {
        this.patient = patient;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.treatmentDetails = treatmentDetails;
    }

    // Getters and Setters
    public int getRecordId() { return recordId; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Date getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(Date admissionDate) { this.admissionDate = admissionDate; }
    public Date getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(Date dischargeDate) { this.dischargeDate = dischargeDate; }
    public String getTreatmentDetails() { return treatmentDetails; }
    public void setTreatmentDetails(String treatmentDetails) { this.treatmentDetails = treatmentDetails; }
}
