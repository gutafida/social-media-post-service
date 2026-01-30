package com.revature.postservice.dto;

import java.time.Instant;

public record PostResponse(
        Long id,
        Long authorId,
        String content,
        String imageUrl,
        Instant createdAt
) {
}
