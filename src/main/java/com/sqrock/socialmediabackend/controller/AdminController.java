package com.sqrock.socialmediabackend.controller;

import com.sqrock.socialmediabackend.dto.ApiDtos.UserResponse;
import com.sqrock.socialmediabackend.service.PostService;
import com.sqrock.socialmediabackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final PostService postService;

    public AdminController(
            UserService userService,
            PostService postService
    ) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/users")
    public List<UserResponse> users() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> removePost(
            @PathVariable Long postId
    ) {
        postService.adminDeletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
