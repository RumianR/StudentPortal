package com.soen343.demo.service;

import com.soen343.demo.model.User;
import com.soen343.demo.repository.CourseRepository;
import com.soen343.demo.repository.RoleRepository;
import com.soen343.demo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Before
    public void setUp() {
        initMocks(this);
        userServiceUnderTest = new UserService(mockUserRepository, mockRoleRepository, mockCourseRepository, mockBCryptPasswordEncoder);

        user = User.builder()
                .id(1)
                .firstName("Unit")
                .lastName("Test")
                .email("unitTest@gmail.com")
                .build();

        Mockito.when(mockUserRepository.save(any()))
                .thenReturn(user);
        Mockito.when(mockUserRepository.findByEmail(anyString()))
                .thenReturn(user);
    }

    @Test
    public void findUserByEmail() {
        final String email = "unitTest@gmail.com";
        User result = userServiceUnderTest.findUserByEmail(email);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void saveUser() {
        // Setup
        final String name = "Unit";

        // Run the test
        User result = userServiceUnderTest.saveUser(user);

        // Verify the results
        assertEquals(name, result.getFirstName());
    }
}