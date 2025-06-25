package com.hospital.repository;

import com.hospital.model.PatientTest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientTestRepository extends JpaRepository<PatientTest, Integer> {
    @Query("SELECT pt FROM PatientTest pt " +
            "WHERE pt.patient.id = ?1 ")
    List<PatientTest> findByPatientPatientId(int patientId);

    List<PatientTest> findByPatientId(int patientId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PatientTest pt WHERE pt.patient.id = ?1")
    void deleteByPatientId(int patientId);
}
