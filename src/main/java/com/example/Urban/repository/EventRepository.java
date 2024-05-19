package com.example.Urban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Urban.entity.EventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Integer > {

    @Query("select case when count(e) > 0 then true else false end from event e where e.employee.id = :employeeId AND e.day = :day")
    boolean existsByEmployeeIdAndDay(@Param("employeeId") int employeeId,
                                     @Param("day")Date day);
    List<EventEntity> findByEmployeeId(int employeeId);
}
