package com.example.moodin.test;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class testController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("ok", true, "msg", "pong");
    }

    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> body) {
        return Map.of("ok", true, "received", body);
    }
}
