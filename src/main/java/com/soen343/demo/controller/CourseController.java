package com.soen343.demo.controller;
import com.soen343.demo.model.User;
import com.soen343.demo.model.Course;
import com.soen343.demo.service.CourseService;
import com.soen343.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CourseController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected CourseService courseService;

    private static StringBuilder conflictMessage = new StringBuilder("The following courses have not been added to due conflict(s) with the your current schedule: ");
    private static StringBuilder conflictMessage2 = new StringBuilder("The following selected courses have not been added to due time conflict(s): ");
    private static StringBuilder conflictMessage3 = new StringBuilder("Some courses have not been added to due maximum course load.");
    private static boolean userCourseConflictError = false;
    private static boolean checkBoxConflictError = false;
    private static boolean maxCourseLoad = false;

    @RequestMapping(value="/student/enroll", method = RequestMethod.GET)
    public ModelAndView enroll(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("courses", user.getCourses());
        modelAndView.addObject("max", user.getCourses().size() > 4? true: false);
        modelAndView.setViewName("student/enroll");
        if(userCourseConflictError) {
            modelAndView.addObject("conflictMessage",  conflictMessage);
            userCourseConflictError = false;
        }
        if(checkBoxConflictError) {
            modelAndView.addObject("conflictMessage2",  conflictMessage2);
            checkBoxConflictError = false;
        }
        if(maxCourseLoad) {
            modelAndView.addObject("conflictMessage3",  conflictMessage3);
            maxCourseLoad = false;
        }
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
        addCourses(user, filterSelectedConflictingCourses(user, checkboxValue));
        modelAndView.setViewName("redirect:/student/enroll"); // Need redirect to refresh the /enroll page
        return modelAndView;
    }

    private List<String> filterSelectedConflictingCourses(User user, String[] checkboxValue){

        List<String> checkboxValueList = Arrays.asList(checkboxValue);
        List<String> conflictedIdsList = new ArrayList<>();

        for(Course c: checkboxConflictCourseList(checkboxValueList)){ // Add the ids of all selected checkbox conflicts to the conflictedIdsList
            conflictedIdsList.add(String.valueOf(c.getId()));
        }
        return checkboxValueList.stream().filter(id -> !conflictedIdsList.contains(id)).collect(Collectors.toList()); // checkboxValueList has no conflicting courses from the selected checkboxes
    }

    private void addCourses(User user, List<String> checkboxValueList) {

        //if user list is empty, all courses can be added
        if(user.getCourses().isEmpty()) {
            for(String s: checkboxValueList) {
                Course course_to_add = courseService.findCourseById(Integer.parseInt(s));
                if(user.getCourses().size() >= 5) {
                    maxCourseLoad = true;
                    return;
                }
                userService.addCourse(user, course_to_add.getId());
            }
            return;
        }

        Course found = new Course();
        conflictMessage = new StringBuilder("The following courses have not been added to due conflict(s) with the your current schedule: ");
        HashMap<Integer, Boolean> hashMap = new HashMap<>();
        List<Course> userCourseList = new ArrayList<>();

        for (Course c : user.getCourses())
            userCourseList.add(c);

        for (int i = 0; i < checkboxValueList.size();i++)
        {
            found = courseService.findCourseById(Integer.parseInt(checkboxValueList.get(i)));
            for(int j = 0; j < userCourseList.size(); j++)
            {
                if(isTImeConflict(userCourseList.get(j),found))
                {
                    hashMap.put(found.getId(), false);
                    break;
                }
                else {
                    hashMap.put(found.getId(), true);
                }
            }

            for (Map.Entry<Integer, Boolean> entry : hashMap.entrySet()) {
                if (entry.getValue() == true) {
                    if(user.getCourses().size() >= 5) {
                        maxCourseLoad = true;
                        return;
                    }
                    userService.addCourse(user, found.getId());
                }
                else {
                    conflictMessage.append(found.getCode() + "-" + found.getSection() + " ");
                    userCourseConflictError = true;
                }
            }

        }
    }

    private List<Course> checkboxConflictCourseList(List<String> checkboxValueList) {
        Course course1 = new Course();
        Course course2 = new Course();
        List<Course> conflictCourseList = new ArrayList<>();
        conflictMessage2 = new StringBuilder("The following selected courses have not been added to due time conflict(s): ");

        if(checkboxValueList != null){
            for(int i=0; i<checkboxValueList.size();++i){
                for(int j=i+1;j<checkboxValueList.size();++j)
                {
                    course1 = courseService.findCourseById(Integer.parseInt(checkboxValueList.get(i)));
                    course2 = courseService.findCourseById(Integer.parseInt(checkboxValueList.get(j)));
                    if(isTImeConflict(course1,course2))
                    {
                        conflictMessage2.append(course1.getCode() + " " + course2.getCode() + " ");
                        conflictCourseList.add(course1);
                        conflictCourseList.add(course2);
                        checkBoxConflictError = true;
                    }
                }
            }
        }
        return conflictCourseList;
    }

    private boolean isTImeConflict(Course course1, Course course2){
        List<String> courseDaysList1 = getCourseDays(course1);
        List<String> courseDaysList2 = getCourseDays(course2);
        if((courseDaysList1.stream().anyMatch(day -> courseDaysList2.contains(day))) &&
                ((course1.getTimeStart().compareTo(course2.getTimeStart()) > 0) && (course1.getTimeStart().compareTo(course2.getTimeEnd()) < 0) ||
                (course2.getTimeStart().compareTo(course1.getTimeStart()) > 0) && (course2.getTimeStart().compareTo(course1.getTimeEnd()) < 0))
        )
        {
            return true;
        }
        return false;
    }

    private List<String> getCourseDays(Course course) {
        List<String> daysList = new ArrayList<>();
        if(course.getTime().contains("Mo"))
            daysList.add("Mo");
        if(course.getTime().contains("Tu"))
            daysList.add("Tu");
        if(course.getTime().contains("We"))
            daysList.add("We");
        if(course.getTime().contains("Th"))
            daysList.add("Th");
        if(course.getTime().contains("Fr"))
            daysList.add("Fr");
        return daysList;
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
    public ModelAndView removecourse(@RequestParam("courseCheckbox") String[] checkboxValue, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<String> removedCoursesID = new ArrayList<String>();
        removedCoursesID = Arrays.asList(checkboxValue);
        List<String> removedCoursesName = new ArrayList<String>();

        for(String s: removedCoursesID) {
            Course c = courseService.findCourseById(Integer.parseInt(s));
            removedCoursesName.add(c.getCode() + "-" + c.getSection());
        }
        String message = "The following courses have been successfully removed: " + String.join(", ", removedCoursesName);
        userService.removeCourses(user, checkboxValue);
        redirectAttributes.addFlashAttribute("removedCourseMessage", message);
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
        courseService.saveEditedCourse(course);
        modelAndView.setViewName("redirect:/faculty/managecourses"); // Need redirect to refresh the faculty/manage page
        return modelAndView;
    }
}
