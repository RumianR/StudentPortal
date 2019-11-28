package com.soen343.demo.repository;
import com.soen343.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("courseRepository")

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByIdIn(int[] ids);


    Course findCourseById(int id);
}
