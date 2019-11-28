package com.soen343.demo.controller;
import com.soen343.demo.model.User;
import com.soen343.demo.model.Course;
import com.soen343.demo.service.CourseService;
import com.soen343.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

@Controller
public class CourseController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected CourseService courseService;

    @RequestMapping(value="/enroll", method = RequestMethod.GET)
    public ModelAndView enroll(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("courses", user.getCourses());
        modelAndView.setViewName("enroll");
        return modelAndView;
    }

    @RequestMapping(value="/enroll/addcourse", method = RequestMethod.GET)
    public ModelAndView addcoursePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Set<Course> c = new HashSet<>(courseService.listAll());
        c.removeAll(user.getCourses());
        modelAndView.addObject("courses", c);
        modelAndView.setViewName("course/addcourse");
        return modelAndView;
    }

    @RequestMapping(value="/enroll/addcourse", method = RequestMethod.POST)
    public ModelAndView addcourse(@RequestParam("courseCheckbox") String[] checkboxValue){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        userService.addCourses(user, checkboxValue);
        modelAndView.setViewName("redirect:/enroll"); // Need redirect to refresh the /enroll page
        return modelAndView;
    }


    @RequestMapping(value="/enroll/removecourse", method = RequestMethod.GET)
    public ModelAndView removecoursePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("courses", user.getCourses());
        modelAndView.setViewName("course/removecourse");
        return modelAndView;
    }

    @RequestMapping(value="/enroll/removecourse", method = RequestMethod.POST)
    public ModelAndView removecourse(@RequestParam("courseCheckbox") String[] checkboxValue){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        userService.removeCourses(user, checkboxValue);
        modelAndView.setViewName("redirect:/enroll");
        return modelAndView;
    }

    @RequestMapping(value="/enroll/search", method = RequestMethod.POST)
    public ModelAndView searchcourse(@RequestParam("courseIdentifier") String courseIdentifier){
        ModelAndView modelAndView = new ModelAndView();
        Set<Course> courses = new HashSet<>();
        for(Course c : courseService.listAll()) {
            if (c.getCode().contains(courseIdentifier.toUpperCase()))
                courses.add(c);
        }
        modelAndView.addObject("courses", courses);
        modelAndView.setViewName("course/addcourse");
        return modelAndView;
    }
}
