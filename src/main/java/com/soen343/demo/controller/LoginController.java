package com.soen343.demo.controller;

import com.soen343.demo.model.Course;
import com.soen343.demo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Set;


import java.util.HashSet;

@Controller
public class LoginController extends BaseController {



    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/student/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("student/home");
        return modelAndView;
    }

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
        modelAndView.addObject("courses", courseService.listAll());
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
