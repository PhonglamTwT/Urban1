package com.example.Urban.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity(name="employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="image")
    String image;

    @Column(name="name")
    String name;

    @Column(name="email")
    String email;

    @Column(name="phone")
    String phone;

    @Column(name="gender")
    String gender;

    @Column(name="address")
    String address;

    @Column(name="position")
    String position;

    @Column(name="headquarter")
    String headquarter;

    @ToString.Exclude
    @OneToMany(mappedBy = "employee")
    private List<EventEntity> events;

    @ToString.Exclude
    @OneToOne(mappedBy = "employee")
    private AccountEntity account;

    public int Getid(){
        return id;
    }
    public void Setid(int id){
        this.id=id;
    }

}
