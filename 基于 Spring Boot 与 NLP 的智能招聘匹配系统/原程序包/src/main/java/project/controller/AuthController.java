package project.controller;

import project.dto.ApiResponse;
import project.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(userService.register(body));
    }


    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(userService.login(body.get("account"), body.get("password")));

    }
}