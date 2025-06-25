package com.hospital.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

class MedicalEquipmentBuilderTest {

    @Test
    void testBuilderPattern() {
        Date currentDate = new Date();
        
        // Test successful builder construction
        MedicalEquipment ventilator = new MedicalEquipment.Builder()
            .name("Ventilator")
            .model("VT-2000")
            .manufacturer("MedTech")
            .purchaseDate(currentDate)
            .location("ICU-1")
            .status("Active")
            .serialNumber("VT2000-123")
            .requiresCertification(true)
            .build();

        // Verify all properties are set correctly
        assertEquals("Ventilator", ventilator.getName());
        assertEquals("VT-2000", ventilator.getModel());
        assertEquals("MedTech", ventilator.getManufacturer());
        assertEquals(currentDate, ventilator.getPurchaseDate());
        assertEquals("ICU-1", ventilator.getLocation());
        assertEquals("Active", ventilator.getStatus());
        assertEquals("VT2000-123", ventilator.getSerialNumber());
        assertTrue(ventilator.isRequiresCertification());

        // Test validation of required fields
        assertThrows(IllegalStateException.class, () -> {
            new MedicalEquipment.Builder()
                .model("VT-2000")
                .build();
        });
    }
}