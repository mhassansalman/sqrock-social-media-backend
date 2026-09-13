package com.sqrock.socialmediabackend.controller;

import com.sqrock.socialmediabackend.dto.ApiDtos.UpdateProfileRequest;
import com.sqrock.socialmediabackend.dto.ApiDtos.UserResponse;
import com.sqrock.socialmediabackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        return userService.getOwnProfile(authentication.getName());
    }

    @PatchMapping("/me")
    public UserResponse update(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return userService.updateProfile(
                authentication.getName(),
                request
        );
    }

    @GetMapping("/{username}")
    public UserResponse profile(@PathVariable String username) {
        return userService.getProfile(username);
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<Void> follow(
            Authentication authentication,
            @PathVariable String username
    ) {
        userService.follow(
                authentication.getName(),
                username
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<Void> unfollow(
            Authentication authentication,
            @PathVariable String username
    ) {
        userService.unfollow(
                authentication.getName(),
                username
        );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/followers")
    public List<UserResponse> followers(
            @PathVariable String username
    ) {
        return userService.followers(username);
    }

    @GetMapping("/{username}/following")
    public List<UserResponse> following(
            @PathVariable String username
    ) {
        return userService.following(username);
    }
}
