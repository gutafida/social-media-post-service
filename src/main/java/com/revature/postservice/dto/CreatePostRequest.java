package com.revature.postservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(
        @NotBlank
        String content,
        String imageUrl
) {
}
