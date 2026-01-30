package com.revature.postservice.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        Long postId,
        Long authorId,
        String content,
        Instant createdAt
) {
}
