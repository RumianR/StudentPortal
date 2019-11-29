package com.soen343.demo.controller;
import com.soen343.demo.model.User;
import com.soen343.demo.model.Course;
import com.soen343.demo.service.CourseService;
import com.soen343.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @RequestMapping(value="/student/enroll", method = RequestMethod.GET)
    public ModelAndView enroll(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("courses", user.getCourses());
        modelAndView.addObject("max", user.getCourses().size() > 4? true: false);
        modelAndView.setViewName("student/enroll");
        return modelAndView;
    }

    @RequestMapping(value="/student/enroll/addcourse", method = RequestMethod.GET)
    public ModelAndView addcoursePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Set<Course> c = new HashSet<>(courseService.listAll());
        c.removeAll(user.getCourses());
        modelAndView.addObject("courses", c);
        modelAndView.setViewName("student/addcourse");
        return modelAndView;
    }

    @RequestMapping(value="/student/enroll/addcourse", method = RequestMethod.POST)
    public ModelAndView addcourse(@RequestParam("courseCheckbox") String[] checkboxValue){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        userService.addCourses(user, checkboxValue);
        modelAndView.setViewName("redirect:/student/enroll"); // Need redirect to refresh the /enroll page
        return modelAndView;
    }


    @RequestMapping(value="/student/enroll/removecourse", method = RequestMethod.GET)
    public ModelAndView removecoursePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("courses", user.getCourses());
        modelAndView.setViewName("student/removecourse");
        return modelAndView;
    }

    @RequestMapping(value="/student/enroll/removecourse", method = RequestMethod.POST)
    public ModelAndView removecourse(@RequestParam("courseCheckbox") String[] checkboxValue){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        userService.removeCourses(user, checkboxValue);
        modelAndView.setViewName("redirect:/student/enroll");
        return modelAndView;
    }

    @RequestMapping(value="/student/enroll/search", method = RequestMethod.POST)
    public ModelAndView searchcourse(@RequestParam("courseIdentifier") String courseIdentifier){
        ModelAndView modelAndView = new ModelAndView();
        Set<Course> courses = new HashSet<>(courseService.listAll());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        courses.removeAll(user.getCourses());
        Set<Course> output = new HashSet<>();
        for(Course c : courses) {
            if (c.getCode().contains(courseIdentifier.toUpperCase()))
                output.add(c);
        }
        modelAndView.addObject("courses", output);
        modelAndView.setViewName("student/addcourse");
        return modelAndView;
    }


    @RequestMapping(value="/faculty/managecourses", method = RequestMethod.GET)
    public ModelAndView facultyManageCourses(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<Course> courses = new HashSet<>(courseService.listAll());
        modelAndView.addObject("courses", courses);
        modelAndView.setViewName("faculty/manage");
        return modelAndView;
    }


    @RequestMapping(value="/faculty/addcourse", method = RequestMethod.GET)
    public ModelAndView facultyAddcoursePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("course", new Course());
        modelAndView.setViewName("faculty/addcourse");
        return modelAndView;
    }

    @RequestMapping(value="/faculty/addcourse", method = RequestMethod.POST)
    public ModelAndView facultyAddcourse(@ModelAttribute Course course){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        courseService.saveCourse(course);
        modelAndView.setViewName("redirect:/faculty/managecourses"); // Need redirect to refresh the faculty/manage page
        return modelAndView;
    }


    @RequestMapping(value="/faculty/removecourse", method = RequestMethod.GET)
    public ModelAndView facultyRemovecoursePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("courses", courseService.listAll());
        modelAndView.setViewName("faculty/removecourse");
        return modelAndView;
    }

    @RequestMapping(value="/faculty/removecourse", method = RequestMethod.POST)
    public ModelAndView facultyRemovecourse(@RequestParam("courseCheckbox") String[] checkboxValue){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        courseService.removeCourses(checkboxValue);
        modelAndView.setViewName("redirect:/faculty/managecourses"); // Need redirect to refresh the faculty/manage page
        return modelAndView;
    }

    @RequestMapping(value="/faculty/chosecourse", method = RequestMethod.GET)
    public ModelAndView facultyChosecoursePage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("courses", courseService.listAll());
        modelAndView.setViewName("faculty/chosecourse");
        return modelAndView;
    }

    @RequestMapping(value="/faculty/chosecourse", method = RequestMethod.POST)
    public ModelAndView facultyChosecoursePage(@RequestParam("courseRadiovalue") String radioValue){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("course", courseService.findCourseById(Integer.parseInt(radioValue)));
        modelAndView.setViewName("faculty/editcourse");
        return modelAndView;
    }


    @RequestMapping(value="/faculty/editcourse", method = RequestMethod.POST)
    public ModelAndView facultyEditcourse(@ModelAttribute Course course){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        courseService.saveCourse(course);
        modelAndView.setViewName("redirect:/faculty/managecourses"); // Need redirect to refresh the faculty/manage page
        return modelAndView;
    }
}
