package com.tyc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentDto {
    @NotNull
    String userId;
    @NotNull
    String postId;
    @NotNull
    @NotBlank
    String content;
    String username;
}
