package com.soen343.studentapp;


import com.soen343.studentapp.service.CourseServiceTest;
import com.soen343.studentapp.service.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        UserServiceTest.class,
        CourseServiceTest.class,
})

public class ServiceTestSuite {
}
