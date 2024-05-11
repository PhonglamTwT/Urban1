package com.example.Urban.repository;

import com.example.Urban.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Urban.entity.EmployeeEntity;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer > {

}
