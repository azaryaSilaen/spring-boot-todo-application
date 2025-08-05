package com.example.springBootTodoApplication.controllers;/*
Author: Azarya Silaen
 */

import com.example.springBootTodoApplication.services.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    private TodoItemService todoItemService;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("todoItems",todoItemService.getAll());
        //25:40 - should have appeared on index.html automatically based on the video
        // but for some reason the htlm didn't recognize this attribute
        return modelAndView;
    }

}
