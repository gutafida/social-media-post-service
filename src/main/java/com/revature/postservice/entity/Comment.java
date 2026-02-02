package com.revature.postservice.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "post_id", nullable = false)
    private Long postId;
    @Column(nullable = false, name = "author_id")
    private String authorUsername;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Comment() {
    }
    public Comment(Long postId, String authorUsername, String content, Instant createdAt) {
        this.postId = postId;
        this.authorUsername = authorUsername;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
