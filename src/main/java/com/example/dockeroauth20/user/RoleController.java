package com.example.dockeroauth20.user;

import com.example.dockeroauth20.user.dto.RoleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value = RoleController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoleController {
    static final String REST_URL = "/api/roles";
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Get all role list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got all role list",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Wrong credentials",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<?> getRoles() {
        var roles = roleService.findAll();
        return ResponseEntity.ok(
                roles.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    private RoleDto convertToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

}

