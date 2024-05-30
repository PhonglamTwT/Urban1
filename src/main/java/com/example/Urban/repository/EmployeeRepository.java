package com.example.Urban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.Urban.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer > {
    @Query(value = "SELECT e.name,e.email,e.phone,e.position,e.headquarter FROM EmployeeEntity e INNER JOIN EventEntity ev ON e.id = ev.employee_id WHERE (e.name = :name OR e.headquarter = :headquarter OR e.position = :position) AND e.day =:day LIMIT 1",nativeQuery = true)
    Optional<EmployeeEntity> searchEmployee (@Param("name")String name,
                                             @Param("headquarter")String headquarter,
                                             @Param("position")String position,
                                             @Param("day") Date day);
    @Query(value = "SELECT e.id, e.name, e.email, e.phone, e.position, e.headquarter,e.address, e.gender,e.image " +
            "FROM employee e " +
            "INNER JOIN event ev ON e.id = ev.employee_id " +
            "WHERE ev.day = :day", nativeQuery = true)
    List<EmployeeEntity> getByDay(@Param("day") LocalDate day);






    Optional<EmployeeEntity> findByEmail(String email);
}
