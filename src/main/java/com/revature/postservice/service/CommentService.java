package com.revature.postservice.service;

import com.revature.postservice.dto.CommentResponse;
import com.revature.postservice.dto.CreateCommentRequest;
import com.revature.postservice.entity.Comment;
import com.revature.postservice.repository.CommentRepository;
import com.revature.postservice.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public CommentResponse addComment(Long postId, String authorUsername, CreateCommentRequest request) {
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        Comment saved = commentRepository.save(
                new Comment(postId, authorUsername, request.content(), Instant.now())
        );

        return toResponse(saved);
    }

    public List<CommentResponse> listByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthorUsername(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}