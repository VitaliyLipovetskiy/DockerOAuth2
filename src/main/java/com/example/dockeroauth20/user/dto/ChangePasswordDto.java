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
public class ChangePasswordDto {
    @NotBlank(message = "Token can't be blank")
    @NotNull(message = "Token can't be null")
    @Size(min = 36, max =  36, message = "Token must be 36 characters")
    private String token;

    @NotBlank(message = "Password can't be blank")
    @NotNull(message = "Password can't be null")
    @Size(min = 8, max =  30, message = "Password must be between 8 and 30 characters")
    private String password;
}