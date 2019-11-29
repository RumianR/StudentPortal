package com.soen343.demo.service;

import com.soen343.demo.model.Course;
import com.soen343.demo.model.Role;
import com.soen343.demo.model.User;
import com.soen343.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("courseService")
public class CourseService {

    private CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> listAll(){
        List<Course> courses = new ArrayList<>();
        courseRepository.findAll().forEach(courses::add);
        return courses;
    }

    public Course findCourseById(int id){
        return courseRepository.findCourseById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void removeCourses(String[] CoursesRequest) {


        List<Integer> courseIds = new ArrayList<>();

        for (int i = 0; i < CoursesRequest.length; i++) {
            courseRepository.deleteById(Integer.parseInt(CoursesRequest[i]));
        }



    }

}
