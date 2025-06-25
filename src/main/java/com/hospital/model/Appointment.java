package com.hospital.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    private Slot slot;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    // Constructors
    public Appointment() {}

    public Appointment(Patient patient, Slot slot, String notes) {
        this.patient = patient;
        this.slot = slot;
        this.notes = notes;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public Patient getPatient() { return patient; }
    public Slot getSlot() { return slot; }
    public String getNotes() { return notes; }
    public Date getCreatedAt() { return createdAt; }

    public void setPatient(Patient patient) { this.patient = patient; }
    public void setSlot(Slot slot) { this.slot = slot; }
    public void setNotes(String notes) { this.notes = notes; }
}