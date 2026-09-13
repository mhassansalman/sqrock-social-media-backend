package com.sqrock.socialmediabackend.controller;

import com.sqrock.socialmediabackend.dto.ApiDtos.CommentRequest;
import com.sqrock.socialmediabackend.dto.ApiDtos.CommentResponse;
import com.sqrock.socialmediabackend.dto.ApiDtos.LikeResponse;
import com.sqrock.socialmediabackend.dto.ApiDtos.PostRequest;
import com.sqrock.socialmediabackend.dto.ApiDtos.PostResponse;
import com.sqrock.socialmediabackend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> create(
            Authentication authentication,
            @Valid @RequestBody PostRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.createPost(
                        authentication.getName(),
                        request
                ));
    }

    @GetMapping("/posts")
    public List<PostResponse> allPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{postId}")
    public PostResponse post(@PathVariable Long postId) {
        return postService.getPostResponse(postId);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse update(
            Authentication authentication,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest request
    ) {
        return postService.updatePost(
                authentication.getName(),
                postId,
                request
        );
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> delete(
            Authentication authentication,
            @PathVariable Long postId
    ) {
        postService.deletePost(
                authentication.getName(),
                postId
        );
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/likes")
    public LikeResponse like(
            Authentication authentication,
            @PathVariable Long postId
    ) {
        return postService.likePost(
                authentication.getName(),
                postId
        );
    }

    @DeleteMapping("/posts/{postId}/likes")
    public LikeResponse unlike(
            Authentication authentication,
            @PathVariable Long postId
    ) {
        return postService.unlikePost(
                authentication.getName(),
                postId
        );
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> comment(
            Authentication authentication,
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.addComment(
                        authentication.getName(),
                        postId,
                        request
                ));
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> comments(
            @PathVariable Long postId
    ) {
        return postService.getComments(postId);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            Authentication authentication,
            @PathVariable Long commentId
    ) {
        postService.deleteComment(
                authentication.getName(),
                commentId
        );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/feed")
    public List<PostResponse> feed(
            Authentication authentication
    ) {
        return postService.getFeed(authentication.getName());
    }
}
