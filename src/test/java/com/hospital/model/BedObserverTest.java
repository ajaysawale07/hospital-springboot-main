package com.hospital.model;

import com.hospital.observer.BedStatusObserver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BedObserverTest {

    private String lastUpdatedBedNumber;
    private String lastUpdatedStatus;

    @Test
    void testObserverPattern() {
        // Create a test observer
        BedStatusObserver testObserver = (bedNumber, status) -> {
            lastUpdatedBedNumber = bedNumber;
            lastUpdatedStatus = status;
        };

        // Create a bed and add the observer
        Bed bed = new Bed("TEST-101", Bed.Status.AVAILABLE);
        bed.addObserver(testObserver);

        // Change bed status
        bed.setStatus(Bed.Status.OCCUPIED);

        // Verify that observer was notified
        assertEquals("TEST-101", lastUpdatedBedNumber);
        assertEquals("OCCUPIED", lastUpdatedStatus);

        // Test observer removal
        bed.removeObserver(testObserver);
        bed.setStatus(Bed.Status.AVAILABLE);
        
        // Values should not change after observer removal
        assertEquals("TEST-101", lastUpdatedBedNumber);
        assertEquals("OCCUPIED", lastUpdatedStatus);
    }
}