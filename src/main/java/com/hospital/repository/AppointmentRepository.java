package com.hospital.repository;

import com.hospital.model.Appointment;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    // You can add custom query methods here if needed
    // For example, find appointments by patient ID or doctor ID
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = ?1")
    List<Appointment> findByPatientId(int patientId);
   @Query("SELECT a FROM Appointment a JOIN a.slot s WHERE s.doctor.id = ?1")
    List<Appointment> findByDoctorId(int doctorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.patient.id = ?1")
    void deleteByPatientId(int patientId);
}
