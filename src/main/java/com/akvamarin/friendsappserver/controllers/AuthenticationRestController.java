package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.AuthServerToken;
import com.akvamarin.friendsappserver.domain.dto.AuthUserParamDTO;
import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.error.ValidationErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.request.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.services.AuthenticationUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Контроллер для регистрации, авторизации
 * и аутентификации пользователей
 * взаимодействует с сервисом authenticationUserService.
 *
 * @see AuthenticationUserService
 * */
@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationUserService authenticationUserService;

    /**
     * Осуществление входа пользователя в приложение.
     *
     * @param paramDTO - параметры пользователя, необходимые для входа (login, pass)
     * @return токен
     */
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

    /**
     * Вход пользователя с использованием профиля социальной сети "ВКонтакте" (OAuth2).
     *
     * @param userSocialDTO - пользователь с информацией полученной из социальной сети
     * @return токен сервера авторизации
     */
    @Operation(
            summary = "Sign in user with Auth2",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format",
                            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(value = "/oauth2", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthServerToken> loginWithOauth2(@RequestBody @Valid AuthUserSocialDTO userSocialDTO) {
        try {
            AuthServerToken authServerToken = authenticationUserService.authOAuth2(userSocialDTO);
            log.info("Method *** loginWithOauth2 *** : HttpStatus = {} ", HttpHeaders.AUTHORIZATION);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, authServerToken.getToken())
                    .body(authServerToken);
        } catch (BadCredentialsException ex) {
            log.info("Method *** loginWithOauth2 *** : HttpStatus = {} ", HttpStatus.UNAUTHORIZED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userDTO - данные пользователя, полученные при заполнении регистрационной формы
     *
     * @return ответ с созданным пользователем
     */
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
        final User user = authenticationUserService.registration(userDTO);

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
