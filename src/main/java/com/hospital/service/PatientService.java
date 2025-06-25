package com.hospital.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.model.Patient;
import com.hospital.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllUsers() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(int patientId){
        return patientRepository.findById(patientId);
    }
}
