package com.tyc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestDto {
    @NotNull
    String token;
    @Size(min = 3, max = 20, message = "Kullanıcı adı en az 3 karakter en fazla 20 karakter olabilir")
    String username;
    String name;
    @Email(message = "E mail formata uygun değil")
    String email;
    String phone;
    String photo;
    String address;
    String about;
}
