package com.cgi.retrospecto.backend.controller;


import com.cgi.retrospecto.backend.domain.RetroBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class BackendController {

    private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

    public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";

    @ResponseBody
    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String sayHello() {
        LOG.info("GET called on /hello resource");
        return HELLO_TEXT;
    }
}
