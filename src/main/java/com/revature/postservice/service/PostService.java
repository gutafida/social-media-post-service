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
    public PostResponse create(Long authorId, CreatePostRequest request){
        Post post = new Post(authorId, request.content(), request.imageUrl(), Instant.now());
        Post savedPost = postRepository.save(post);
        return new PostResponse(
                savedPost.getId(),
                savedPost.getAuthorId(),
                savedPost.getContent(),
                savedPost.getImageUrl(),
                savedPost.getCreatedAt()
        );
    }

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return new PostResponse(
                post.getId(),
                post.getAuthorId(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt());
    }

    public List<PostResponse> list(Long authorId, List<Long> authorIds, int limit){
        if(limit <= 0 || limit > 200){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit must be between 1 and 200");
        }
        if(authorId != null && authorIds != null && !authorIds.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide either authorId or authorIds, not both");
        }
        List<Post> posts;
        if(authorIds != null && !authorIds.isEmpty()){
            posts = postRepository.findByAuthorIdInOrderByCreatedAtDesc(authorIds);
        } else if (authorId != null) {
            posts = postRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);

        }else {
            posts = postRepository.findAllByOrderByCreatedAtDesc();
        }
        return posts.stream().limit(limit).map(this::toResponse).toList();
    }
    public void delete(Long postId, Long currentUserId){
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if(!post.getAuthorId().equals(currentUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this post");
        }
        postRepository.deleteById(postId);
    }

    public boolean exists(Long id){
        return postRepository.existsById(id);
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(post.getId(),
                post.getAuthorId(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt());
    }
}
