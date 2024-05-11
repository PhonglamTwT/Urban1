package com.example.Urban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Urban.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Integer > {
    
}
