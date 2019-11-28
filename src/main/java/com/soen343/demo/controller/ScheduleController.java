package com.soen343.demo.controller;
import com.soen343.demo.model.User;
import com.soen343.demo.model.Course;
import com.soen343.demo.service.CourseService;
import com.soen343.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Set;

@Controller
public class ScheduleController {

    @Autowired
    protected UserService userService;
    @Autowired
    protected CourseService courseService;

    @RequestMapping(value="/viewschedule", method = RequestMethod.GET)
    public ModelAndView viewSchedulePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Set<Course> courses = user.getCourses();
        ArrayList<Course> MoCourses = new ArrayList<>();
        ArrayList<Course> TuCourses = new ArrayList<>();
        ArrayList<Course> WeCourses = new ArrayList<>();
        ArrayList<Course> ThCourses = new ArrayList<>();
        ArrayList<Course> FrCourses = new ArrayList<>();

        for (Course course: courses) {
            if(course.getTime().contains("Mo")){
                MoCourses.add(course);
            }
            if(course.getTime().contains("Tu")){
                TuCourses.add(course);
            }
            if(course.getTime().contains("We")){
                WeCourses.add(course);
            }
            if(course.getTime().contains("Th")){
                ThCourses.add(course);
            }
            if(course.getTime().contains("Fr")){
                FrCourses.add(course);
            }
        }
        modelAndView.addObject("Mo", MoCourses);
        modelAndView.addObject("Tu", TuCourses);
        modelAndView.addObject("We", WeCourses);
        modelAndView.addObject("Th", ThCourses);
        modelAndView.addObject("Fr", FrCourses);

        modelAndView.addObject("User",  user.getFirstName() + "'s Schedule");

        modelAndView.setViewName("student/schedule");
        return modelAndView;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/greeting")
    public ModelAndView test(@RequestParam("courseid") String courseid){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("course", courseService.findCourseById(Integer.parseInt(courseid)));
        modelAndView.setViewName("student/courseDetails");
        return modelAndView;
    }
}
