package com.pendezzapizza.pendezzapizza_api.api;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/redirect")
public class RedirectTestController {

    @GetMapping
    public Map<String, String> captureCode(@RequestParam("code") String code) {
        return Collections.singletonMap("code", code);
    }

}
