package com.jsong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 2020/5/12 10:48
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/{page}")
    public String page(@PathVariable("page") String page) {
        return page;
    }

}
