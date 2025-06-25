package com.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.model.MedicalRecord;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> getMedicalRecordByPatientId(int patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public List<MedicalRecord> getMedicalRecordByDoctorId(int doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId);
    }

    public List<MedicalRecord> getActiveRecordsByDoctorId(int doctorId) {
        return medicalRecordRepository.findPendingPatients(doctorId);
    }
}
