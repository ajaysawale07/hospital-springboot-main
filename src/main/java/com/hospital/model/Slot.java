package com.hospital.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "slots")
public class Slot {

    // Enum for slot status
    public enum SlotStatus {
        AVAILABLE,
        BOOKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private int slotId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id", nullable = false)
    public Doctor doctor;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "slot_number", nullable = false)
    private int slotNumber; // 1-6

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('AVAILABLE', 'BOOKED') DEFAULT 'AVAILABLE'")
    private SlotStatus status;

    // Constructors
    public Slot() {}

    public Slot(Doctor doctor, Date date, int slotNumber, SlotStatus status) {
        this.doctor = doctor;
        this.date = date;
        this.slotNumber = slotNumber;
        this.status = status;
    }

    // Getters and Setters
    public int getSlotId() { return slotId; }
    public Doctor getDoctor() { return doctor; }
    public Date getDate() { return date; }
    public int getSlotNumber() { return slotNumber; }
    public SlotStatus getStatus() { return status; }

    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public void setDate(Date date) { this.date = date; }
    public void setSlotNumber(int slotNumber) { this.slotNumber = slotNumber; }
    public void setStatus(SlotStatus status) { this.status = status; }
}

