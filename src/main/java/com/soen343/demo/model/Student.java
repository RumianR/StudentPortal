package com.soen343.demo.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="user_id")
@Table(name = "student")

public class Student extends User{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "student_id"
    )
    private long studentId;

    @Column(
            name = "balance"
    )
    private double balance;
}
