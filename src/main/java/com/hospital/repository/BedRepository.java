package com.hospital.repository;

import com.hospital.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {
    // You can add custom query methods here if needed
}
