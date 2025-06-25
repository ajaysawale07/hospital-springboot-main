package com.hospital.observer;

import org.springframework.stereotype.Component;

@Component
public class NurseStationObserver implements BedStatusObserver {
    @Override
    public void update(String bedNumber, String status) {
        // In a real application, this could notify the nurse station dashboard
        System.out.println("Nurse Station notified: Bed " + bedNumber + " is now " + status);
    }
}