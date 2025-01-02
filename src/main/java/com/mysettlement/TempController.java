package com.mysettlement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TempController {

    @GetMapping
    public String tempCon() {
        return "YOU ARE IN";
    }
}
