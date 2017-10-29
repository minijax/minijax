package com.example.benchmarks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class HelloSpringBootBenchmark {

    @RequestMapping("/")
    @ResponseBody
    public static String hello() {
        return "Hello World";
    }

    public static void main(final String[] args) {
        SpringApplication.run(HelloSpringBootBenchmark.class, args);
    }
}
