package com.hospital.repository;

import com.hospital.model.Nurse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {
    // You can add custom query methods here if needed
    //Find by least current_patient_count
    @Query(value = "SELECT * FROM nurses ORDER BY current_patient_count ASC LIMIT 1", nativeQuery = true)
    Nurse findNurseWithLeastPatients();
}
