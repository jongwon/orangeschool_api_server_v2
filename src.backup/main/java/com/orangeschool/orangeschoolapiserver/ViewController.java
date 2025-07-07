package com.orangeschool.orangeschoolapiserver;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewController {
    @RequestMapping(value = {"/api"})
    @ResponseBody()
    public String index() {
        return "hello world";
    }
}
