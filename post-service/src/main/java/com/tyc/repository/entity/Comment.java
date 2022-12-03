package com.tyc.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Comment {
    @Id
    String id;
    String userId;
    String postId;
    String content;
    String username;
    int like;
    int dislike;
    @Builder.Default
    long sharedTime = System.currentTimeMillis();
}
