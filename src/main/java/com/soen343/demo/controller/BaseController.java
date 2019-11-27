package com.soen343.demo.controller;

import com.soen343.demo.model.User;
import com.soen343.demo.service.CourseService;
import com.soen343.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


public class BaseController{

    @Autowired
    protected UserService userService;
    @Autowired
    protected CourseService courseService;

}