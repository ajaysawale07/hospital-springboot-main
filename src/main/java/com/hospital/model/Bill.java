package com.hospital.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "generated_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date generatedDate;

    @Column(name = "doctor_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal doctorFee;

    @Column(name = "bed_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal bedFee;

    @Column(name = "test_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal testCharges;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    public enum Status {
        PENDING, PAID
    }

    // Constructors
    public Bill() {}

    public Bill(Patient patient, Date generatedDate, BigDecimal doctorFee, BigDecimal bedFee, BigDecimal testCharges) {
        this.patient = patient;
        this.generatedDate = generatedDate;
        this.doctorFee = doctorFee;
        this.bedFee = bedFee;
        this.testCharges = testCharges;
    }

    public void pay(){
        this.status= Status.PAID;
    }

    // Getters and Setters
    public BigDecimal getTotalAmount() {
        return doctorFee.add(bedFee).add(testCharges);
    }
}
