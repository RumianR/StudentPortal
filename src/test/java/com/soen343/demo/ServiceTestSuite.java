package com.soen343.demo;


import com.soen343.demo.service.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        UserServiceTest.class,
})

public class ServiceTestSuite {
}
