package com.tyc.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivateReguestDto implements Serializable {
    private Long id;
    private String activatedCode;
    private String email;

}
