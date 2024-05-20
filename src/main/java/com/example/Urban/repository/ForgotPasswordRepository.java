package com.example.Urban.repository;

import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.ForgotPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordEntity, Integer> {
    Optional<ForgotPasswordEntity> findByAccount(AccountEntity account);
}
