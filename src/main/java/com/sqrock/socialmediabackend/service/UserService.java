package com.sqrock.socialmediabackend.service;

import com.sqrock.socialmediabackend.dto.ApiDtos.UpdateProfileRequest;
import com.sqrock.socialmediabackend.dto.ApiDtos.UserResponse;
import com.sqrock.socialmediabackend.exception.ApiException;
import com.sqrock.socialmediabackend.model.Follow;
import com.sqrock.socialmediabackend.model.User;
import com.sqrock.socialmediabackend.repository.FollowRepository;
import com.sqrock.socialmediabackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public UserService(
            UserRepository userRepository,
            FollowRepository followRepository
    ) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public UserResponse getOwnProfile(String email) {
        return toResponse(getUserByEmail(email));
    }

    public UserResponse getProfile(String username) {
        return toResponse(getUserByUsername(username));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse updateProfile(
            String currentEmail,
            UpdateProfileRequest request
    ) {
        User user = getUserByEmail(currentEmail);

        if (request.username() != null
                && !request.username().equals(user.getUsername())) {

            if (userRepository.existsByUsername(request.username())) {
                throw new ApiException(
                        HttpStatus.CONFLICT,
                        "Username already exists"
                );
            }

            user.setUsername(request.username());
        }

        if (request.email() != null
                && !request.email().equals(user.getEmail())) {

            if (userRepository.existsByEmail(request.email())) {
                throw new ApiException(
                        HttpStatus.CONFLICT,
                        "Email already exists"
                );
            }

            user.setEmail(request.email());
        }

        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }

        if (request.bio() != null) {
            user.setBio(request.bio());
        }

        if (request.profileImageUrl() != null) {
            user.setProfileImageUrl(request.profileImageUrl());
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void follow(String currentEmail, String targetUsername) {

        User follower = getUserByEmail(currentEmail);
        User following = getUserByUsername(targetUsername);

        if (follower.getId().equals(following.getId())) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot follow yourself"
            );
        }

        if (followRepository.existsByFollowerIdAndFollowingId(
                follower.getId(),
                following.getId()
        )) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "You already follow this user"
            );
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(
            String currentEmail,
            String targetUsername
    ) {
        User follower = getUserByEmail(currentEmail);
        User following = getUserByUsername(targetUsername);

        followRepository.findByFollowerIdAndFollowingId(
                follower.getId(),
                following.getId()
        ).ifPresent(followRepository::delete);
    }

    public List<UserResponse> followers(String username) {

        User user = getUserByUsername(username);

        return followRepository
                .findByFollowingId(user.getId())
                .stream()
                .map(Follow::getFollower)
                .map(this::toResponse)
                .toList();
    }

    public List<UserResponse> following(String username) {

        User user = getUserByUsername(username);

        return followRepository
                .findByFollowerId(user.getId())
                .stream()
                .map(Follow::getFollowing)
                .map(this::toResponse)
                .toList();
    }

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    private User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    private UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getBio(),
                user.getProfileImageUrl(),
                user.getRole()
        );
    }
}