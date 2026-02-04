package com.revature.postservice.controller;

import com.revature.postservice.dto.CommentResponse;
import com.revature.postservice.dto.CreateCommentRequest;
import com.revature.postservice.dto.CreatePostRequest;
import com.revature.postservice.dto.PostResponse;
import com.revature.postservice.service.CommentService;
import com.revature.postservice.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreatePostRequest request) {

        String username = requireUsername(jwt);
        return postService.create(username, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }


    @GetMapping
    public List<PostResponse> list(
            @RequestParam(required = false) String authorUsername,
            @RequestParam(required = false) List<String> authorUsernames,
            @RequestParam(defaultValue = "50") int limit) {

        return postService.list(authorUsername, authorUsernames, limit);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {

        String currentUsername = requireUsername(jwt);
        postService.delete(id, currentUsername);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody CreateCommentRequest request) {

        String username = requireUsername(jwt);
        return commentService.addComment(id, username, request);
    }

    @GetMapping("/{id}/comments")
    public List<CommentResponse> listComments(@PathVariable Long id) {
        return commentService.listByPostId(id);
    }

    // This is for social-service
    @GetMapping("/internal/{id}/exists")
    public Map<String, Boolean> exists(@PathVariable Long id) {
        return Map.of("exists", postService.exists(id));
    }

    private static String requireUsername(Jwt jwt) {
        if (jwt == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String username = jwt.getSubject(); // sub=username
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing subject");
        }
        return username;
    }
}