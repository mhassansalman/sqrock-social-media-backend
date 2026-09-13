package com.sqrock.socialmediabackend.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "post_likes",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"post_id", "user_id"}
        )
)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PostLike() {
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }
}