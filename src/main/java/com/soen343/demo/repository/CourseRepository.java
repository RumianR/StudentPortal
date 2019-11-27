package com.soen343.demo.repository;
import com.soen343.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")

public interface CourseRepository extends JpaRepository<Course, Long> {

}
