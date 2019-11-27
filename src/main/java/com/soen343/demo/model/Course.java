package com.soen343.demo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private int id;
    @Column(name = "instructor")
    private String instructor;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "time")
    private String time;
    @Column(name = "location")
    private String location;
    @Column(name = "section")
    private String section;
    @Column(name = "term")
    private String term;

}
