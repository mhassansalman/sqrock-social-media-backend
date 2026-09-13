package com.sqrock.socialmediabackend.repository;

import com.sqrock.socialmediabackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    @Query("""
            select p
            from Post p
            where p.author.id = :userId
               or p.author.id in (
                    select f.following.id
                    from Follow f
                    where f.follower.id = :userId
               )
            order by p.createdAt desc
            """)
    List<Post> findFeed(@Param("userId") Long userId);
}