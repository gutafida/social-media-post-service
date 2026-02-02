package com.revature.postservice.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        Long postId,
        String authorUsername,
        String content,
        Instant createdAt
) {
}
