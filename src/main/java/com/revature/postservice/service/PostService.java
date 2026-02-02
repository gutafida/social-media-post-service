package com.revature.postservice.service;

import com.revature.postservice.dto.CreatePostRequest;
import com.revature.postservice.dto.PostResponse;
import com.revature.postservice.entity.Post;
import com.revature.postservice.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
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

    // ✅ Option 2: list by username(s), not id(s)
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

    // ✅ delete by postId, verify authorUsername matches currentUsername
    public void delete(Long postId, String currentUsername) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!post.getAuthorUsername().equals(currentUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this post");
        }

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
