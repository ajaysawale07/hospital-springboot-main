-- Hospital Management System Database Schema
-- For MariaDB (to be sourced directly)

-- Create database and user
DROP DATABASE IF EXISTS hospital_management1;
CREATE DATABASE hospital_management1;
USE hospital_management1;

-- Base User Table (for all roles)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(8) NOT NULL,  -- plaintext password (8 chars)
    role ENUM('RECEPTIONIST', 'DOCTOR', 'NURSE', 'PATIENT') NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER')
);

-- Patient Table
CREATE TABLE patients (
    patient_id INT PRIMARY KEY,
    admission_date DATE,
    discharge_date DATE,
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Doctor Table
CREATE TABLE doctors (
    doctor_id INT PRIMARY KEY,
    specialization VARCHAR(100) NOT NULL,
    fee DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Nurse Table
CREATE TABLE nurses (
    nurse_id INT PRIMARY KEY,
    current_patient_count INT DEFAULT 0 CHECK (current_patient_count <= 10),
    FOREIGN KEY (nurse_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Receptionist Table
CREATE TABLE receptionists (
    receptionist_id INT PRIMARY KEY,
    FOREIGN KEY (receptionist_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bed Table
CREATE TABLE beds (
    bed_id INT AUTO_INCREMENT PRIMARY KEY,
    bed_number VARCHAR(20) UNIQUE NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED') DEFAULT 'AVAILABLE'
);

-- Medical Record Table
CREATE TABLE medical_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    treatment_details TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- Test Table
CREATE TABLE tests (
    test_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL
);

-- Patient Tests (Many-to-Many)
CREATE TABLE patient_tests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    test_id INT NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (test_id) REFERENCES tests(test_id)
);

--Slots
CREATE TABLE slots (
    slot_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    date DATE NOT NULL,
    slot_number TINYINT NOT NULL CHECK (slot_number BETWEEN 1 AND 6),
    status ENUM('AVAILABLE', 'BOOKED') DEFAULT 'BOOKED',
    UNIQUE KEY (doctor_id, date, slot_number),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- Appointments
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    slot_id INT NOT NULL,
    notes TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (slot_id) REFERENCES slots(slot_id)
);

-- Nurse-Patient Assignments
CREATE TABLE nurse_assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nurse_id INT NOT NULL,
    patient_id INT NOT NULL,
    FOREIGN KEY (nurse_id) REFERENCES nurses(nurse_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    UNIQUE KEY unique_nurse_patient (nurse_id, patient_id)
);

-- Bed Assignments
CREATE TABLE bed_assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    bed_id INT NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (bed_id) REFERENCES beds(bed_id)
);

-- Bills
CREATE TABLE bills (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    generated_date DATE NOT NULL,
    doctor_fee DECIMAL(10, 2) NOT NULL,
    bed_fee DECIMAL(10, 2) NOT NULL,
    test_charges DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) GENERATED ALWAYS AS (doctor_fee + bed_fee + test_charges) STORED,
    status ENUM('PENDING', 'PAID') DEFAULT 'PENDING',
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);



-- Insert initial data
-- Sample beds
INSERT INTO beds (bed_number, status) VALUES 
('A101', 'AVAILABLE'),
('A102', 'AVAILABLE'),
('B101', 'AVAILABLE'),
('B102', 'AVAILABLE'),
('C101', 'AVAILABLE'),
('C102', 'AVAILABLE');

-- Sample tests
INSERT INTO tests (name, cost) VALUES
('Blood Test', 25.50),
('X-Ray', 120.00),
('MRI', 450.00),
('Urine Analysis', 15.75);

-- Sample users (with plaintext passwords)
-- Receptionist
INSERT INTO users (username, password, role, name, gender) VALUES
('reception1', 'pass1234', 'RECEPTIONIST', 'Nancy', 'FEMALE');

-- Doctors
INSERT INTO users (username, password, role, name, gender) VALUES
('dr_smith', 'doc12345', 'DOCTOR', 'Dr. Smith', 'MALE'),
('dr_jones', 'doc54321', 'DOCTOR', 'Dr. Jones', 'FEMALE');

INSERT INTO doctors (doctor_id, specialization, fee) VALUES
(2, 'Cardiology', 200.00),
(3, 'Neurology', 250.00);

-- Nurses
INSERT INTO users (username, password, role, name, gender) VALUES
('nurse1', 'nur12345', 'NURSE', 'Nurse Johnson', 'FEMALE'),
('nurse2', 'nur54321', 'NURSE', 'Nurse Williams', 'MALE');

INSERT INTO nurses (nurse_id, current_patient_count) VALUES
(4, 0),
(5, 0);

insert into receptionists (receptionist_id) values
(1);

-- Sample patient
INSERT INTO users (username, password, role, name, gender) VALUES
('patient1', 'pat12345', 'PATIENT', 'John Doe', 'MALE');

INSERT INTO patients (patient_id, admission_date) VALUES
(6, CURDATE());

DELIMITER $$

CREATE TRIGGER before_insert_slots
BEFORE INSERT ON slots
FOR EACH ROW
BEGIN
    DECLARE next_slot_number TINYINT;
    
    -- Find the highest slot_number for the given doctor and date
    SELECT COALESCE(MAX(slot_number), 0) + 1 INTO next_slot_number
    FROM slots
    WHERE doctor_id = NEW.doctor_id AND date = NEW.date;

    -- Ensure the slot_number doesn't exceed 6
    IF next_slot_number > 6 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Maximum slots (6) already allocated for this doctor on this date';
    ELSE
        SET NEW.slot_number = next_slot_number;
    END IF;
END $$

DELIMITER ;
