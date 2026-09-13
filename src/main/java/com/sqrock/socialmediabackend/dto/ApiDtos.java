package com.sqrock.socialmediabackend.dto;

import com.sqrock.socialmediabackend.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class ApiDtos {

    private ApiDtos() {
    }

    public record RegisterRequest(
            @NotBlank
            @Size(min = 3, max = 30)
            String username,

            @NotBlank
            @Size(max = 100)
            String fullName,

            @NotBlank
            @Email
            String email,

            @NotBlank
            @Size(min = 6, max = 100)
            String password
    ) {
    }

    public record LoginRequest(
            @NotBlank
            @Email
            String email,

            @NotBlank
            String password
    ) {
    }

    public record AuthResponse(
            String token,
            UserResponse user
    ) {
    }

    public record UpdateProfileRequest(
            @Size(min = 3, max = 30)
            String username,

            @Size(min = 1, max = 100)
            String fullName,

            @Email
            String email,

            @Size(max = 160)
            String bio,

            @Size(max = 500)
            String profileImageUrl
    ) {
    }

    public record UserResponse(
            Long id,
            String username,
            String fullName,
            String email,
            String bio,
            String profileImageUrl,
            Role role
    ) {
    }

    public record PostRequest(
            @NotBlank
            @Size(max = 2000)
            String content
    ) {
    }

    public record PostResponse(
            Long id,
            String content,
            String authorUsername,
            LocalDateTime timestamp,
            long likesCount,
            long commentsCount
    ) {
    }

    public record CommentRequest(
            @NotBlank
            @Size(max = 1000)
            String content
    ) {
    }

    public record CommentResponse(
            Long id,
            String content,
            String authorUsername,
            LocalDateTime timestamp
    ) {
    }

    public record LikeResponse(
            long likesCount,
            boolean liked
    ) {
    }
}