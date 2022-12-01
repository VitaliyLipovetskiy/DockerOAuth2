package com.example.dockeroauth20.user.dto;

import com.example.dockeroauth20.validation.NoHtml;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ForgotPasswordDto {
    @Email(message = "Email should be in right format")
    @NoHtml
    @NotBlank(message = "Email can't be blank")
    @NotNull(message = "Email can't be null")
    private String email;
}
