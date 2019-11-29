package com.soen343.demo.service;

import com.soen343.demo.model.Course;
import com.soen343.demo.repository.CourseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.MockitoAnnotations.initMocks;

public class CourseServiceTest {

    @Mock
    private CourseRepository mockCourseRepository;

    private CourseService courseServiceUnderTest;
    private Course course1;
    private List<Course> courseList = new ArrayList<>();


    private final String course1_id = "1";
    private final String course1_instructor = "Unit";
    private final String course1_name = "Unit Course 1";
    private final String course1_code = "UNIT";



    @Before
    public void setUp() {
        initMocks(this);

        courseServiceUnderTest = new CourseService(mockCourseRepository);

        course1 = Course.builder()
                .id(Integer.parseInt(course1_id))
                .instructor(course1_instructor)
                .name(course1_name)
                .code(course1_code)
                .build();

        courseList.add(course1);

        Mockito.when(mockCourseRepository.findAll())
                .thenReturn(courseList);


        Mockito.when(mockCourseRepository.findCourseById(anyInt()))
                .thenReturn(course1);
    }


    @Test
    public void listAll() {

        List<Course> result = courseServiceUnderTest.listAll();

        Course c1 = result.get(0);

        assertEquals(c1.getId(), Integer.parseInt(course1_id));
        assertEquals(c1.getInstructor(), course1_instructor);
        assertEquals(c1.getName(), course1_name);
        assertEquals(c1.getCode(), course1_code);
        assertEquals(c1.getName(), course1_name);

    }

    @Test
    public void findCourseById() {

        Course result = courseServiceUnderTest.findCourseById(Integer.parseInt(course1_id));
        assertEquals(result.getId(), Integer.parseInt(course1_id));
        assertEquals(result.getInstructor(), course1_instructor);
        assertEquals(result.getName(), course1_name);
        assertEquals(result.getCode(), course1_code);
        assertEquals(result.getName(), course1_name);

    }
}