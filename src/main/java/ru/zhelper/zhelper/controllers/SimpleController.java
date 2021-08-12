package ru.zhelper.zhelper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SimpleController {
    private static final String indexPageName="index";

    @GetMapping
    public String get() {
        return indexPageName;
    }
}
