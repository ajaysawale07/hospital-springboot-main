package com.hospital.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
    // You can add custom query methods here if needed
}
