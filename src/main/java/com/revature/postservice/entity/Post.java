package com.revature.postservice.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_username", nullable = false)
    private String authorUsername;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Post() {
    }
    public Post(String authorUsername, String content, String imageUrl, Instant createdAt) {
        this.authorUsername = authorUsername;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public String getAuthorUsername() {
        return authorUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }

}
