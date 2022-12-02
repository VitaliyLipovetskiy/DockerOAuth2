package com.example.dockeroauth20.user;

import com.example.dockeroauth20.user.dto.*;
import com.example.dockeroauth20.validation.exceptions.ApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserController {
    static final String REST_URL = "/api/users";
    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final String REFERER_HEADER = "referer";

    @Operation(summary = "Sign up new user to work with API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Wrong credentials",
                    content = @Content)})
    @PostMapping("/signup")
    @Validated
    public ResponseEntity<?> registerUser(
            HttpServletRequest request,
            @RequestBody @Valid LoginRequest loginRequest) {
        log.info("register {}", loginRequest);
        User signup = userService.signup(loginRequest);
        UserDto userDto = convertToDto(signup);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get information about current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Wrong credentials",
                    content = @Content)})
    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            HttpServletRequest request) {
        Optional<User> result = userService.findByEmail(request.getUserPrincipal().getName());
        if (result.isPresent()) {
            User user = result.get();
            user.setPassword(null);
            return ResponseEntity.ok(convertToDto(user));
        }
        throw new ApplicationException(HttpStatus.UNAUTHORIZED, "Unknown user");
    }

    @Operation(summary = "Check token for email address verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully verified",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Verification code not found",
                    content = @Content)})
    @PutMapping(value = "/verify")
    public ResponseEntity<?> emailVerify(
            @Valid @RequestBody TokenDto dto) {
        return ResponseEntity.ok(convertToDto(userService.emailVerify(dto.getToken())));
    }

    @Operation(summary = "Check token and change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Verification code not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content)})
    @PutMapping(value = "/password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordDto dto) {
        return ResponseEntity.ok(convertToDto(userService.changePassword(dto.getToken(), dto.getPassword())));
    }

    @Operation(summary = "Check token and change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "404", description = "User with specified email not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content)})
    @PostMapping(value = "/forgot")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody ForgotPasswordDto dto,
            HttpServletRequest request) {
        var host = request.getHeader(REFERER_HEADER) != null ? request.getHeader(REFERER_HEADER) : request.getRemoteHost();
        return ResponseEntity.ok(convertToDto(userService.forgotPassword(dto.getEmail(), host)));
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
