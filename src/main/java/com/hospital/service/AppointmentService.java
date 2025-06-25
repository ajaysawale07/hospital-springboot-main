package com.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.hospital.model.Appointment;
import com.hospital.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    // Add methods to handle appointment-related logic here

    public List<Appointment> getAllPatientAppointments(int patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAllDoctorAppointments(int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
}
