package com.soen343.studentapp.service;

import com.soen343.studentapp.model.Course;
import com.soen343.studentapp.repository.CourseRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.MockitoAnnotations.initMocks;

public class CourseServiceTest {

    @Mock
    private CourseRepository mockCourseRepository;

    private CourseService courseServiceUnderTest;
    private Course course;
    private List<Course> courseList = new ArrayList<>();

    private static final String[] courseRequest = {"1", "2"};
    private static final String course_id = "1";
    private static final String course_instructor = "Unit";
    private static final String course_name = "Unit Course 1";
    private static final String course_code = "UNIT";
    private static final String course_time = "Mo,Fr,16:45-18:00";



    @Before
    public void setUp() {
        initMocks(this);

        courseServiceUnderTest = new CourseService(mockCourseRepository);

        course = Course.builder()
                .id(Integer.parseInt(course_id))
                .instructor(course_instructor)
                .name(course_name)
                .code(course_code)
                .time(course_time)
                .build();

        courseList.add(course);

        Mockito.when(mockCourseRepository.findAll())
                .thenReturn(courseList);

        Mockito.when(mockCourseRepository.findCourseById(anyInt()))
                .thenReturn(course);

        Mockito.when(mockCourseRepository.save(any()))
                .thenReturn(course);

    }


    @Test
    public void listAll() {
        List<Course> result = courseServiceUnderTest.listAll();

        Course c1 = result.get(0);

        assertEquals(c1.getId(), Integer.parseInt(course_id));
        assertEquals(c1.getInstructor(), course_instructor);
        assertEquals(c1.getName(), course_name);
        assertEquals(c1.getCode(), course_code);
        assertEquals(c1.getName(), course_name);
        assertEquals(c1.getTime(), course_time);
    }

    @Test
    public void findCourseById() {
        Course result = courseServiceUnderTest.findCourseById(Integer.parseInt(course_id));
        assertEquals(result.getId(), Integer.parseInt(course_id));
        assertEquals(result.getInstructor(), course_instructor);
        assertEquals(result.getName(), course_name);
        assertEquals(result.getCode(), course_code);
        assertEquals(result.getName(), course_name);
        assertEquals(result.getTime(), course_time);

    }

    @Test
    public void saveCourse() {
        String resultTime = "Mo Fr - 16:45-18:00";
        Course result = courseServiceUnderTest.saveCourse(course);
        assertEquals(result.getId(), Integer.parseInt(course_id));
        assertEquals(result.getInstructor(), course_instructor);
        assertEquals(result.getName(), course_name);
        assertEquals(result.getCode(), course_code);
        assertEquals(result.getName(), course_name);
        assertEquals(result.getTime(), resultTime);
    }

    @Test
    public void saveCourseEmptyTime() {
        Course course2 = Course.builder()
                .id(Integer.parseInt(course_id))
                .instructor(course_instructor)
                .name(course_name)
                .code(course_code)
                .time("")
                .build();
        Course result = courseServiceUnderTest.saveCourse(course2);
        assertNull(result);
    }

    @Test
    public void saveCourseNullTime() {
        Course course2 = Course.builder()
                .id(Integer.parseInt(course_id))
                .instructor(course_instructor)
                .name(course_name)
                .code(course_code)
                .build();
        Course result = courseServiceUnderTest.saveCourse(course2);
        assertNull(result);
    }

    @Test
    public void saveCourseNull() {
        Course result = courseServiceUnderTest.saveCourse(null);
        assertNull(result);
    }

    @Test
    public void saveEditedCourse() {
        Course result = courseServiceUnderTest.saveEditedCourse(course);
        assertEquals(result.getId(), Integer.parseInt(course_id));
        assertEquals(result.getInstructor(), course_instructor);
        assertEquals(result.getName(), course_name);
        assertEquals(result.getCode(), course_code);
        assertEquals(result.getName(), course_name);
        assertEquals(result.getTime(), course_time);

    }

    @Test
    public void removeCourses() {
        courseServiceUnderTest.removeCourses(courseRequest);
        assertNotNull(courseServiceUnderTest);
    }
}