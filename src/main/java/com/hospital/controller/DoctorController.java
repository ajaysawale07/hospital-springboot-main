package com.hospital.controller;

import com.hospital.model.*;
import com.hospital.repository.*;
import com.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private PatientTestRepository patientTestRepository;

    @GetMapping("/viewapp")
    public String viewAppointments(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        List<Appointment> appointments = appointmentService.getAllDoctorAppointments(user.getId());
        if (appointments == null) {
            appointments = Collections.emptyList(); // Use an empty list if no appointments found
        }

        model.addAttribute("user", user);
        model.addAttribute("appointments", appointments);
        return "viewdocapp";
    }

    @GetMapping("/createrecord")
    public String showCreateRecordForm(HttpSession session, Model model) {
        // Get the logged-in user
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Get doctor information
        Doctor doctor = doctorService.getDoctorById(user.getId());
        if (doctor == null) {
            return "redirect:/error";
        }

        // Get all appointments for the doctor to populate patient dropdown
        List<Appointment> doctorAppointments = appointmentService.getAllDoctorAppointments(doctor.getDoctorId());

        // Get all available tests
        List<Test> availableTests = testService.getAllTests();

        model.addAttribute("doctor", doctor);
        model.addAttribute("doctorAppointments", doctorAppointments);
        model.addAttribute("availableTests", availableTests);

        return "createrec";
    }

    @PostMapping("/createrecord")
    public String createMedicalRecord(
            @RequestParam("patient.patientId") int patientId,
            @RequestParam("admissionDate") String admissionDateStr,
            @RequestParam("treatmentDetails") String treatmentDetails,
            @RequestParam(value = "testIds", required = false) List<Integer> testIds,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            // Get the logged-in user
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                return "redirect:/login";
            }

            // Validate that the patient exists
            Optional<Patient> optionalPatient = patientService.getPatientById(patientId);
            if (optionalPatient.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Patient not found");
                return "redirect:/doctor/createrecord";
            }
            Patient patient = optionalPatient.get();

            // Parse the admission date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date admissionDate = dateFormat.parse(admissionDateStr);

            // Create new medical record
            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setPatient(patient);
            medicalRecord.setAdmissionDate(admissionDate);
            medicalRecord.setTreatmentDetails(treatmentDetails);

            // Save the medical record
            MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);

            // Process test assignments if any were selected
            if (testIds != null && !testIds.isEmpty()) {
                for (Integer testId : testIds) {
                    Optional<Test> optionalTest = testService.getTestById(testId);
                    if (optionalTest.isPresent()) {
                        Test test = optionalTest.get();

                        // Create and save patient-test relationship
                        PatientTest patientTest = new PatientTest();
                        patientTest.setPatient(patient);
                        patientTest.setTest(test);
                        patientTestRepository.save(patientTest);
                    }
                }
                redirectAttributes.addFlashAttribute("success", "Medical record created successfully with " + testIds.size() + " prescribed tests");
            } else {
                redirectAttributes.addFlashAttribute("success", "Medical record created successfully");
            }

            return "redirect:/doctor/viewrecords";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating medical record: " + e.getMessage());
            return "redirect:/doctor/createrecord";
        }
    }

    @GetMapping("/viewrecords")
    public String viewMedicalRecords(HttpSession session, Model model) {
        // Get the logged-in user
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Get doctor information
        Doctor doctor = doctorService.getDoctorById(user.getId());
        if (doctor == null) {
            return "redirect:/error";
        }

        // Get all medical records for patients associated with this doctor
        List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecordByDoctorId(doctor.getDoctorId());

        model.addAttribute("doctor", doctor);
        model.addAttribute("medicalRecords", medicalRecords);

        return "viewdocrecords";
    }


    @Autowired
    private BedAssignmentRepository bedAssignmentRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientTestService patientTestService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NurseAssignmentRepository nurseAssignmentRepository;

    @GetMapping("/dischargepatient/{id}")
    public String showDischarge(HttpSession session, Model model, @PathVariable("id") int id) {
        // Get the logged-in user
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Get doctor information
        Doctor doctor = doctorService.getDoctorById(user.getId());
        if (doctor == null) {
            return "redirect:/error";
        }

        Optional<MedicalRecord> recordOptional = medicalRecordRepository.findById(id);

        MedicalRecord record = recordOptional.get();
        record.setDischargeDate(new Date());
        medicalRecordRepository.save(record);


        try {

            Patient patient = record.getPatient();

            // Parse the discharge date
            Date dischargeDate = record.getDischargeDate();
            Date admissionDate = record.getAdmissionDate();

            int days = (int) ((dischargeDate.getTime() - admissionDate.getTime()) / (1000 * 60 * 60 * 24));
            model.addAttribute("days",days);

            // Update patient with discharge date
            patient.setDischargeDate(dischargeDate);
            patientRepository.save(patient);

            // Generate bill

            BigDecimal doctorFee = BigDecimal.valueOf(doctor.getFee());
            double doctoram = doctorFee.doubleValue();
            model.addAttribute("doctoram",doctoram);

            // 2. Calculate test charges
            BigDecimal testCharges = BigDecimal.ZERO;
            List<PatientTest> patientTests = patientTestService.findByPatientId(patient.getPatientId());
            for (PatientTest patientTest : patientTests) {
                Test test = patientTest.getTest();
                testCharges = testCharges.add(test.getCost());
            }

            model.addAttribute("tests",patientTests);

            double testam = testCharges.doubleValue();
            model.addAttribute("testam",testam);

            // 3. Calculate bed fee based on bed type
            BigDecimal bedFee = BigDecimal.ZERO;
            List<BedAssignment> bedAssignments = bedAssignmentRepository.findByPatientId(patient.getPatientId());
            if (!bedAssignments.isEmpty()) {
                BedAssignment bedAssignment = bedAssignments.getFirst(); // Get the most recent assignment
                Optional<Bed> optionalBed = bedRepository.findById(bedAssignment.getBedId());

                if (optionalBed.isPresent()) {
                    Bed bed = optionalBed.get();
                    String bedNumber = bed.getBedNumber();

                    // Calculate bed fee based on the first letter of bed number
                    if (bedNumber != null && !bedNumber.isEmpty()) {
                        char firstLetter = bedNumber.charAt(0);
                        bedFee = switch (firstLetter) {
                            case 'A' -> new BigDecimal(100);
                            case 'B' -> new BigDecimal(200);
                            case 'C' -> new BigDecimal(300);
                            default -> new BigDecimal(100); // Default fee
                        };
                        bedFee = bedFee.multiply(BigDecimal.valueOf(days));
                    }

                    // Update bed status to available
                    bed.setStatus(Bed.Status.AVAILABLE);
                    bedRepository.save(bed);

                    bedAssignmentRepository.deleteByPatientId(bedAssignment.getPatientId());
                }
            }

            double bedam = bedFee.doubleValue();
            model.addAttribute("bedam",bedam);

            // 4. Create and save the bill
            Bill bill = new Bill(patient, new Date(), doctorFee, bedFee, testCharges);
            bill.pay();
            billRepository.save(bill);

            double totalam = doctoram + bedam + testam;
            model.addAttribute("totalam",totalam);

        } catch (Exception e) {
            return "redirect:/doctor/viewrecords";
        }


        // Delete all tests related to this patient from patientTest table
        patientTestRepository.deleteByPatientId(record.getPatient().getId());

        appointmentRepository.deleteByPatientId(record.getPatient().getId());


        nurseAssignmentRepository.deleteByPatientId(record.getPatient().getId());

        model.addAttribute("doctor", doctor);
        model.addAttribute("record", record);

        return "discharge";
    }
}