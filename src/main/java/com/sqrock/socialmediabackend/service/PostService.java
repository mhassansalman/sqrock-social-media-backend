package com.sqrock.socialmediabackend.service;

import com.sqrock.socialmediabackend.dto.ApiDtos.CommentRequest;
import com.sqrock.socialmediabackend.dto.ApiDtos.CommentResponse;
import com.sqrock.socialmediabackend.dto.ApiDtos.LikeResponse;
import com.sqrock.socialmediabackend.dto.ApiDtos.PostRequest;
import com.sqrock.socialmediabackend.dto.ApiDtos.PostResponse;
import com.sqrock.socialmediabackend.exception.ApiException;
import com.sqrock.socialmediabackend.model.Comment;
import com.sqrock.socialmediabackend.model.Post;
import com.sqrock.socialmediabackend.model.PostLike;
import com.sqrock.socialmediabackend.model.User;
import com.sqrock.socialmediabackend.repository.CommentRepository;
import com.sqrock.socialmediabackend.repository.PostLikeRepository;
import com.sqrock.socialmediabackend.repository.PostRepository;
import com.sqrock.socialmediabackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            CommentRepository commentRepository,
            PostLikeRepository postLikeRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public PostResponse createPost(String email, PostRequest request) {
        User user = getUser(email);
        Post post = new Post();
        post.setContent(request.content());
        post.setAuthor(user);
        return toPostResponse(postRepository.save(post));
    }

    @Transactional
    public PostResponse updatePost(
            String email,
            Long postId,
            PostRequest request
    ) {
        User user = getUser(email);
        Post post = getPost(postId);
        requireOwner(post, user);
        post.setContent(request.content());
        return toPostResponse(post);
    }

    @Transactional
    public void deletePost(String email, Long postId) {
        User user = getUser(email);
        Post post = getPost(postId);
        requireOwner(post, user);
        deletePostInternal(post);
    }

    @Transactional
    public void adminDeletePost(Long postId) {
        deletePostInternal(getPost(postId));
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toPostResponse)
                .toList();
    }

    public PostResponse getPostResponse(Long postId) {
        return toPostResponse(getPost(postId));
    }

    public List<PostResponse> getFeed(String email) {
        User user = getUser(email);
        return postRepository.findFeed(user.getId())
                .stream()
                .map(this::toPostResponse)
                .toList();
    }

    @Transactional
    public LikeResponse likePost(String email, Long postId) {
        User user = getUser(email);
        Post post = getPost(postId);

        boolean alreadyLiked =
                postLikeRepository.existsByPostIdAndUserId(
                        postId,
                        user.getId()
                );

        if (!alreadyLiked) {
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(user);
            postLikeRepository.save(like);
        }

        return new LikeResponse(
                postLikeRepository.countByPostId(postId),
                true
        );
    }

    @Transactional
    public LikeResponse unlikePost(String email, Long postId) {
        User user = getUser(email);
        getPost(postId);

        postLikeRepository.findByPostIdAndUserId(
                postId,
                user.getId()
        ).ifPresent(postLikeRepository::delete);

        return new LikeResponse(
                postLikeRepository.countByPostId(postId),
                false
        );
    }

    @Transactional
    public CommentResponse addComment(
            String email,
            Long postId,
            CommentRequest request
    ) {
        User user = getUser(email);
        Post post = getPost(postId);

        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setAuthor(user);
        comment.setPost(post);

        return toCommentResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(String email, Long commentId) {
        User user = getUser(email);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Comment not found"
                ));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "You can only delete your own comment"
            );
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponse> getComments(Long postId) {
        getPost(postId);

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toCommentResponse)
                .toList();
    }

    private void requireOwner(Post post, User user) {
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "You can only modify your own post"
            );
        }
    }

    private void deletePostInternal(Post post) {
        commentRepository.deleteByPostId(post.getId());
        postLikeRepository.deleteByPostId(post.getId());
        postRepository.delete(post);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "Post not found"
                ));
    }

    private PostResponse toPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                postLikeRepository.countByPostId(post.getId()),
                commentRepository.countByPostId(post.getId())
        );
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getCreatedAt()
        );
    }
}
