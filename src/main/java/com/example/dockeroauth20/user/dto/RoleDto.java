package com.example.dockeroauth20.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoleDto {
    private String id;
    private String name;
    private String description;
}
