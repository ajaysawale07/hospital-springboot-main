package com.hospital.repository;

import com.hospital.model.BedAssignment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedAssignmentRepository extends JpaRepository<BedAssignment, Integer> {
    // You can add custom query methods here if needed
    // For example, if you want to find BedAssignment by patient ID or bed ID, you can add methods like:
    List<BedAssignment> findByPatientId(int patientId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BedAssignment where patientId=?1")
    void deleteByPatientId(int patientId);
    
}
