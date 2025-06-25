package com.hospital.repository;

import com.hospital.model.Receptionist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {
    // You can add custom query methods here if needed
}
