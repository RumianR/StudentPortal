package com.soen343.studentapp.service;

import com.soen343.studentapp.model.Course;
import com.soen343.studentapp.model.User;
import com.soen343.studentapp.repository.CourseRepository;
import com.soen343.studentapp.repository.RoleRepository;
import com.soen343.studentapp.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private CourseRepository mockCourseRepository;
    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    private UserService userServiceUnderTest;
    private User user;
    private List<User> userList = new ArrayList<>();


    private Course course;
    private static final String[] courseRequest = {"1", "2"};
    private static final int[] courseIds = {1,2};
    private List<Course> courseList = new ArrayList<>();
    private final String course1_id = "1";
    private final String course1_instructor = "Unit";
    private final String course1_name = "Unit Course 1";
    private final String course1_code = "UNIT";

    private final int id = 1;
    private final String firstName = "Unit";
    private final String lastName = "Test";
    private final String email = "unitTest@gmail.com";


    @Before
    public void setUp() {
        initMocks(this);
        userServiceUnderTest = new UserService(mockUserRepository, mockRoleRepository, mockCourseRepository, mockBCryptPasswordEncoder);
        Set<Course> courses = new HashSet<>();
        int defaultActive = 0;
        user = User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .active(defaultActive)
                .courses(courses)
                .build();

        userList.add(user);

        course = Course.builder()
                .id(Integer.parseInt(course1_id))
                .instructor(course1_instructor)
                .name(course1_name)
                .code(course1_code)
                .build();

        courseList.add(course);

        Mockito.when(mockUserRepository.save(any()))
                .thenReturn(user);

        Mockito.when(mockUserRepository.findByEmail(anyString()))
                .thenReturn(user);

        Mockito.when(mockUserRepository.findAll())
                .thenReturn(userList);

        Mockito.when(mockCourseRepository.findByIdIn(courseIds))
                .thenReturn(courseList);

    }

    @Test
    public void findUserByEmail() {
        User result = userServiceUnderTest.findUserByEmail(email);

        assertEquals(result.getId(), id);
        assertEquals(result.getFirstName(), firstName);
        assertEquals(result.getLastName(), lastName);
        assertEquals(result.getEmail(), email);
    }

    @Test
    public void listAll() {
        List<User> result = userServiceUnderTest.listAll();
        User userResult = result.get(0);

        assertEquals(userResult.getId(), id);
        assertEquals(userResult.getFirstName(), firstName);
        assertEquals(userResult.getLastName(), lastName);
        assertEquals(userResult.getEmail(), email);
    }

    @Test
    public void saveUser() {
        User result = userServiceUnderTest.saveUser(user);

        assertEquals(result.getActive(), 1);
        assertEquals(result.getId(), id);
        assertEquals(result.getFirstName(), firstName);
        assertEquals(result.getLastName(), lastName);
        assertEquals(result.getEmail(), email);
    }

    @Test
    public void saveNullUser() {
        User result = userServiceUnderTest.saveUser(null);
        assertNull(result);
    }


    @Test
    public void addCourses() {

        userServiceUnderTest.addCourses(user, courseRequest);
        Set<Course> currentCourses = user.getCourses();
        Course userCourse = null;
        for(Course c : currentCourses) {
            userCourse = c;
        }

        assertNotNull(userCourse);
        assertEquals(userCourse.getId(), Integer.parseInt(course1_id));
        assertEquals(userCourse.getInstructor(), course1_instructor);
        assertEquals(userCourse.getName(), course1_name);
        assertEquals(userCourse.getCode(), course1_code);
        assertEquals(userCourse.getName(), course1_name);

    }

    @Test
    public void addCoursesNullCourseRequest() {
        userServiceUnderTest.addCourses(user, null);
    }

    @Test
    public void addCoursesEmptyCourseRequest() {
        String[] courseRequest = {""};
        User user = User.builder().build();
        userServiceUnderTest.addCourses(user, courseRequest);
        assertNull(user.getCourses());
    }

    @Test
    public void addCoursesWrongCourseRequest() {
        String[] courseRequest = {"abc"};
        User user = User.builder().build();
        userServiceUnderTest.addCourses(user, courseRequest);
        assertNull(user.getCourses());
    }


    @Test
    public void removeCourses() {
        Set<Course> courses = new HashSet<>();
        courses.add(course);
        user.setCourses(courses);

        userServiceUnderTest.removeCourses(user, courseRequest);
        Set<Course> userCourses = user.getCourses();
        int userCourseSize = userCourses.size();

        assertEquals(0, userCourseSize);
    }

    @Test
    public void removeCoursesNullCourseRequest() {
        userServiceUnderTest.removeCourses(user, null);
    }

    @Test
    public void removeCoursesEmptyCourseRequest() {
        String[] courseRequest = {""};
        User user = User.builder().build();
        userServiceUnderTest.removeCourses(user, courseRequest);
        assertNull(user.getCourses());
    }

    @Test
    public void removeCoursesWrongCourseRequest() {
        String[] courseRequest = {"abc"};
        User user = User.builder().build();
        userServiceUnderTest.removeCourses(user, courseRequest);
        assertNull(user.getCourses());
    }

}