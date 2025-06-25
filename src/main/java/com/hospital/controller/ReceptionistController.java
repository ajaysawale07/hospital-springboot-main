package com.hospital.controller;

import com.hospital.model.*;
import com.hospital.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Controller
@RequestMapping("/receptionist")
public class ReceptionistController {


    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private BedAssignmentRepository bedAssignmentRepository;

    @Autowired
    private NurseAssignmentRepository nurseAssignmentRepository;

    @GetMapping("/registerusers")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("users", usersRepository.findAll()); // List of users for selection
        return "register_users"; // Thymeleaf template name
    }

    @PostMapping("/registerusers")
    @Transactional
    public String registerPatient(@ModelAttribute Users user, @RequestParam("username") String username) {
        Users existingUser = usersRepository.findByUsername(username);

        if (existingUser == null) {
            usersRepository.saveAndFlush(user);
            existingUser = user; // Fetch saved user
        }
        System.out.println("User ID: " + existingUser.getId());
        System.out.println("Hello Worlds from the ReceptionistController");
        System.out.println(usersRepository.toString());

        if(existingUser.getRole() == Users.Role.PATIENT) {
            Date admissionDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(admissionDate.getTime());

            // Direct SQL insert
            String sql = "INSERT INTO patients (patient_id, admission_date) VALUES (?, ?)";
            jdbcTemplate.update(sql, existingUser.getId(), sqlDate);
        }


        else if(existingUser.getRole() == Users.Role.RECEPTIONIST) {
            Receptionist rec = new Receptionist(existingUser.getId());
            receptionistRepository.save(rec);
        }
        else if(existingUser.getRole() == Users.Role.NURSE) {
            Nurse nurse = new Nurse(existingUser.getId()); 
            nurseRepository.save(nurse);
        }
        

        return "redirect:/receptionist/dashboard";
    }


    @GetMapping("/bookslot")
    public String showBookSlotForm(Model model) {
        model.addAttribute("slot", new Slot());
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("patients", patientRepository.findAll());
        return "book_slot"; // Thymeleaf template name
    }

    @PostMapping("/bookslot")
    public String bookSlot(
            @RequestParam("patient.patient_id") int patientId,
            @RequestParam("slot.doctor.doctor_id") int doctorId,
            @RequestParam("slot.date") String appointmentDateStr,
            @RequestParam("slot.status") String statusStr,
            @RequestParam("bed.bed_id") int bedId,
            @RequestParam(value = "appointment.notes", required = false) String notes) {

        try {
            // Convert string date to Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date appointmentDate = dateFormat.parse(appointmentDateStr);

            // Convert string status to enum
            Slot.SlotStatus status = Slot.SlotStatus.valueOf(statusStr);

            // Find the patient
            Optional<Patient> optionalPatient = patientRepository.findById(patientId);
            if (!optionalPatient.isPresent()) {
                return "redirect:/receptionist/bookslot?error=patientNotFound";
            }
            Patient patient = optionalPatient.get();

            // Find the doctor
            Doctor doctor = doctorRepository.findById(doctorId);
            if (doctor == null) {
                return "redirect:/receptionist/bookslot?error=doctorNotFound";
            }

            // Create and save the slot
            Slot slot = new Slot();
            slot.setDoctor(doctor);
            slot.setDate(appointmentDate);
            slot.setStatus(status);

            // The slot_number will be auto-assigned by the database trigger
            Slot savedSlot = slotRepository.save(slot);

            // Create and save the bed assignment
            BedAssignment bedAssignment = new BedAssignment();
            bedAssignment.setBedId(bedId);
            bedAssignment.setPatientId(patient.getPatientId());
            bedAssignmentRepository.save(bedAssignment);

            //Change status of the bed to occupied
            Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new RuntimeException("Bed not found"));
            bed.setStatus(Bed.Status.OCCUPIED);
            bedRepository.save(bed);

            //Assign nurse with least current_patient_count to the patient
            Nurse nurse = nurseRepository.findNurseWithLeastPatients();
            if (nurse != null) {
                NurseAssignment nurseAssignment = new NurseAssignment();
                nurseAssignment.setNurseId(nurse.getNurseId());
                nurseAssignment.setPatientId(patient.getPatientId());
                nurseAssignmentRepository.save(nurseAssignment);

                // Update the nurse's current patient count
                nurse.setCurrentPatientCount(nurse.getCurrentPatientCount() + 1);
                nurseRepository.save(nurse);
            } else {
                return "redirect:/receptionist/bookslot?error=nurseNotFound";
            }


            // Create and save the appointment
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setSlot(savedSlot);
            appointment.setNotes(notes);

            appointmentRepository.save(appointment);

            return "redirect:/receptionist/bookslot?success=true";
        } catch (ParseException e) {
            return "redirect:/receptionist/bookslot?error=invalidDateFormat";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/receptionist/bookslot?error=" + e.getMessage();
        }
    }

}
