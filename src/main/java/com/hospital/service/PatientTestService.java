package com.hospital.service;


import com.hospital.model.Doctor;
import com.hospital.model.PatientTest;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientTestService {

    @Autowired
    private PatientTestRepository patientTestRepository;

    public List<PatientTest> findByPatientId(int patientId) {
        return patientTestRepository.findByPatientId(patientId);
    }


}
