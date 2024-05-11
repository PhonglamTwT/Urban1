package com.example.Urban.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Urban.entity.AccountEntity;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findByUsername(String username);
}
