package com.example.Urban.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;


@Entity(name="forgot_password")
@Data
public class ForgotPasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "otp", nullable = false)
    private int otp;

    @Column(name = "expirationTime", nullable = false)
    private Date expirationTime;

    @OneToOne
    private AccountEntity account;
}
