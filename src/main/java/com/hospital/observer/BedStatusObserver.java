package com.hospital.observer;

public interface BedStatusObserver {
    void update(String bedNumber, String status);
}