package com.example.dockeroauth20.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String email;
    private boolean isActive;
    private String roleName;
    private String roleDescription;
    private String roleId;
    private boolean isVerified;
}
