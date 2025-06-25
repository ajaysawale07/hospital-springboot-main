package com.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.hospital.model.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer>{
    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.id = ?1")
    List<MedicalRecord> findByPatientId(int patientId);

    @Query("SELECT DISTINCT mr FROM MedicalRecord mr " +
           "WHERE mr.patient IN (" +
           "   SELECT a.patient FROM Appointment a " +
           "   WHERE a.slot.doctor.doctorId = :doctorId" +
           ")")
    List<MedicalRecord> findByDoctorId(@Param("doctorId") int doctorId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patient.id = ?1")
    List<MedicalRecord> findByDoctorId1(int doctorId);

    @Query("SELECT DISTINCT mr FROM MedicalRecord mr " +
           "WHERE mr.patient IN (" +
           "   SELECT a.patient FROM Appointment a " +
           "   WHERE a.slot.doctor.doctorId = :doctorId" +
           ") AND mr.dischargeDate = NULL")
    List<MedicalRecord> findPendingPatients(@Param("doctorId") int doctorId);
}
