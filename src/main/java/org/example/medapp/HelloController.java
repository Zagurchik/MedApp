package org.example.medapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class HelloController {
    @GetMapping ("/ping")
    String ping () { return "ok";}

}
