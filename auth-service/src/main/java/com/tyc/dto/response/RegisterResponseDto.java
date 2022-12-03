package com.tyc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDto {
    private Long id;
    private String username;
    private String activatedCode;
    @Builder.Default
    private int code = 200;

}
