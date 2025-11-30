package com.OneBpy.controller;


import com.OneBpy.models.Role;
import com.OneBpy.models.User;
import com.OneBpy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MeController {
    private final UserService userService;

    @GetMapping
    User myProfile() {
        return userService.getCurrentUser();
    }
    @PutMapping("/edit")
    public ResponseEntity<User> editProfile(@RequestBody User updateUser) {
        return ResponseEntity.ok(userService.editProfile(updateUser));
    }

    @GetMapping("/role")
    String getRole() {
        return (userService.getCurrentUser()).getRole();
    }
}
