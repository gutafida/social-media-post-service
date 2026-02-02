package com.revature.postservice.repository;

import com.revature.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByAuthorUsernameOrderByCreatedAtDesc(String authorUsername);

    List<Post> findByAuthorUsernameInOrderByCreatedAtDesc(List<String> authorUsernames);
}

