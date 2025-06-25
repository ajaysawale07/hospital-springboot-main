package com.hospital.repository;


import com.hospital.model.NurseAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseAssignmentRepository extends JpaRepository<NurseAssignment, Integer> {
    // You can add custom query methods here if needed
    @Modifying
    @Transactional
    @Query("DELETE from NurseAssignment where patientId=?1")
    void deleteByPatientId(int patientId);
}
