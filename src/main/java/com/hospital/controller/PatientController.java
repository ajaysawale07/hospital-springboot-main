package com.hospital.controller;

import com.hospital.model.*;
import com.hospital.service.MedicalRecordService;
import com.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.hospital.service.AppointmentService;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/viewapp")
    public String viewAppointments(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        List<Appointment> appointments = appointmentService.getAllPatientAppointments(user.getId());
        if (appointments == null) {
            appointments = Collections.emptyList();
        }
        model.addAttribute("user", user);
        model.addAttribute("appointments", appointments);
        return "viewpatapp";
    }

    @GetMapping("/viewrecords")
    public String viewMedicalRecords(HttpSession session, Model model) {
        // Get the logged-in user
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Get doctor information
        Optional<Patient> opatient = patientService.getPatientById(user.getId());
        if (opatient.isEmpty()) {
            return "redirect:/error";
        }

        Patient patient = opatient.get();

        // Get all medical records for patients associated with this doctor
        List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecordByPatientId(patient.getPatientId());

        model.addAttribute("patient", patient);
        model.addAttribute("medicalRecords", medicalRecords);

        return "viewpatrecords";
    }

}
