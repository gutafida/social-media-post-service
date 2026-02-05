package com.revature.postservice.dto;

import java.time.Instant;

public record PostResponse(
        Long id,
        String authorUsername,
        String content,
        String imageUrl,
        Instant createdAt,
        Long commentCount
) {
}
