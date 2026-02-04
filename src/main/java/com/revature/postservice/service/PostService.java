package com.revature.postservice.service;

import com.revature.postservice.dto.CreatePostRequest;
import com.revature.postservice.dto.PostResponse;
import com.revature.postservice.entity.Post;
import com.revature.postservice.repository.CommentRepository;
import com.revature.postservice.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public PostResponse create(String authorUsername, CreatePostRequest request) {
        Post post = new Post(authorUsername, request.content(), request.imageUrl(), Instant.now());
        Post saved = postRepository.save(post);
        return toResponse(saved);
    }

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return toResponse(post);
    }


    public List<PostResponse> list(String authorUsername, List<String> authorUsernames, int limit) {
        if (limit <= 0 || limit > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be between 1 and 200");
        }
        if (authorUsername != null && authorUsernames != null && !authorUsernames.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide either authorUsername or authorUsernames, not both");
        }

        List<Post> posts;
        if (authorUsernames != null && !authorUsernames.isEmpty()) {
            posts = postRepository.findByAuthorUsernameInOrderByCreatedAtDesc(authorUsernames);
        } else if (authorUsername != null && !authorUsername.isBlank()) {
            posts = postRepository.findByAuthorUsernameOrderByCreatedAtDesc(authorUsername);
        } else {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        }

        return posts.stream().limit(limit).map(this::toResponse).toList();
    }

    public void delete(Long postId, String currentUsername) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!post.getAuthorUsername().equals(currentUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this post");
        }

        commentRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);
    }

    public boolean exists(Long id) {
        return postRepository.existsById(id);
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getAuthorUsername(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt()
        );
    }
}
