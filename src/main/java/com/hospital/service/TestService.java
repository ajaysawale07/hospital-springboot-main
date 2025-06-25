package com.hospital.service;

import com.hospital.model.Test;
import com.hospital.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public Optional<Test> getTestById(int testId){
        return Optional.ofNullable(testRepository.findById(testId));
    }

    public List<Test> getAllTests(){
        return testRepository.findAll();
    }
}
