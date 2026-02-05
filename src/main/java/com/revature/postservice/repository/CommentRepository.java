package com.revature.postservice.repository;

import com.revature.postservice.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    @Transactional
    @Modifying
    void deleteByPostId(Long postId);
    Long countByPostId(Long postId);
}
