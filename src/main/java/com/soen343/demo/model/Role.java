package com.soen343.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roleId")
    private int id;
    @Column(name = "role")
    private String role;
}
