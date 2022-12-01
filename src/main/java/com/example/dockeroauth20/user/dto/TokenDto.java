package com.example.dockeroauth20.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TokenDto {
    @NotBlank(message = "Token can't be blank")
    @NotNull(message = "Token can't be null")
    @Size(min = 36, max =  36, message = "Token must be 36 characters")
    private String token;
}
