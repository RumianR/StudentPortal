package com.soen343.demo.service;


import com.soen343.demo.model.Course;
import com.soen343.demo.model.Role;
import com.soen343.demo.model.User;
import com.soen343.demo.repository.RoleRepository;
import com.soen343.demo.repository.UserRepository;
import com.soen343.demo.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CourseRepository courseRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       CourseRepository courseRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.courseRepository = courseRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> listAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User saveUser(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
        return userRepository.save(user);
    }


    public void addCourses(User user, String[] CoursesRequest) {


        int[] courseIds = new int[CoursesRequest.length];
        for (int i = 0; i < CoursesRequest.length; i++) {
            courseIds[i] = Integer.parseInt(CoursesRequest[i]);
        }

        List<Course> courseList = courseRepository.findByIdIn(courseIds);
        Set<Course> currentCourses = user.getCourses();
        currentCourses.addAll(courseList);
        user.setCourses(currentCourses);

        userRepository.save(user);
    }


    public void removeCourses(User user, String[] CoursesRequest) {


        int[] courseIds = new int[CoursesRequest.length];
        for (int i = 0; i < CoursesRequest.length; i++) {
            courseIds[i] = Integer.parseInt(CoursesRequest[i]);
        }

        List<Course> courseList = courseRepository.findByIdIn(courseIds);
        Set<Course> currentCourses = user.getCourses();
        currentCourses.removeAll(courseList);
        user.setCourses(currentCourses);

        userRepository.save(user);
    }

}