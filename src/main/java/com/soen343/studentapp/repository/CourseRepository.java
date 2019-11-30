package com.soen343.studentapp.repository;
import com.soen343.studentapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("courseRepository")

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByIdIn(int[] ids);


    Course findCourseById(int id);

    @Transactional
    void deleteById(int id);

}
