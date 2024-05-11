package com.example.Urban.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Entity(name="event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="day")
    private Date day;

    @Column(name="worktime")
    private String worktime;

    @Column(name="workplace")
    private String workplace;

    @Column(name="status")
    private String status;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="employee_id")
    private EmployeeEntity employee;

}
