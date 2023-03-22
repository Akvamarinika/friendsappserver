package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.AuthServerToken;
import com.akvamarin.friendsappserver.domain.dto.AuthUserParamDTO;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.dto.responseerror.ValidationErrorResponse;
import com.akvamarin.friendsappserver.services.AuthenticationUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationUserService authenticationUserService;

    @Operation(
            summary = "Sign in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format",
                            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthServerToken> login(@RequestBody @Valid AuthUserParamDTO paramDTO) {
        try {
            AuthServerToken authServerToken = authenticationUserService.authentication(paramDTO);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, authServerToken.getToken())
                    .body(authServerToken);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Register new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User is registered"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format",
                            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registration(@Valid @RequestBody UserDTO userDTO) {
        final User user = authenticationUserService.register(userDTO);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
