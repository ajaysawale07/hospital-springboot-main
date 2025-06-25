package com.hospital.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private int testId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    // Constructors
    public Test() {}

    public Test(String name, BigDecimal cost) {
        this.name = name;
        this.cost = cost;
    }

    // Getters and Setters
    public int getTestId() { return testId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
}
