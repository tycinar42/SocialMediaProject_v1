package com.tyc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostDto {
    String userId;
    String username;
    String title;
    String content;
    String postMediaUrl;
}
