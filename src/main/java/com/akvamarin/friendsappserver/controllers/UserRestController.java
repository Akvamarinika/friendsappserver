package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.error.ErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.error.ValidationErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.request.UserDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Контроллер для работы с
 * пользовательскими данными, взаимодействует с UserService
 *
 * @see UserService
 * */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    /**
     * Создает нового пользователя.
     *
     * @param userDTO - пользователь DTO
     * @return ответ с кодом состояния 201 (Created)
     */
    @Operation(
            summary = "Create new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "New user is created"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(name = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDTO userDTO) {

        final User user = userService.createNewUser(userDTO);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    /**
     * Возвращает список всех пользователей с основными данными
     * для отобрания на интерфейсе (UI).
     *
     * @return список пользователей DTO с основной информацией
     */
    @Operation(
            summary = "Get all slim users",
            responses = @ApiResponse(responseCode = "200",
                    description = "All slim users",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewUserSlimDTO.class))))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ViewUserSlimDTO> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @param id - идентификатор пользователя
     * @return пользователя DTO с основной информацией
     * @throws EntityNotFoundException если пользователь не найден
     */
    @Operation(
            summary = "Find user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User by ID found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewUserDTO getUserByID(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Возвращает пользователя по его логину.
     *
     * @param login - логин пользователя
     * @return пользователь DTO
     * @throws EntityNotFoundException, если пользователь не найден
     */
    @Operation(
            summary = "Get user by login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User by login found", content = @Content(schema = @Schema(implementation = ViewUserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User by login not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/login/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewUserDTO getUserByLogin(@PathVariable String login) {
        return userService.findByLogin(login);
    }

    /**
     * Возвращает основные данные пользователя по его логину.
     *
     * @param login - логин пользователя
     * @return пользователь DTO с основной информацией
     * @throws EntityNotFoundException, если пользователь не найден
     */
    @Operation(
            summary = "Get slim user by login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Slim user by login found", content = @Content(schema = @Schema(implementation = ViewUserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Slim user by login not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/slim/login/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewUserSlimDTO getSlimUserByLogin(@PathVariable String login) {
        return userService.findSlimUserByLogin(login);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param dto - пользователь DTO для обновления
     * @throws EntityNotFoundException, если запрашиваемые данные не найдены
     */
    @Operation(
            summary = "Update existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User for requested ID is updated", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Requested data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@Valid @RequestBody UserDTO dto) {
        userService.updateUser(dto);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id - идентификатор пользователя
     */
    @Operation(
            summary = "Remove user by ID",
            responses = @ApiResponse(responseCode = "204", description = "User for requested ID is removed")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    /**
     * Проверяет, занят ли указанный логин.
     *
     * @param username - логин для проверки
     * @return ответ с кодом состояния 200 (OK) и флагом, указывающим, занят ли логин
     */
    @Operation(
            summary = "Username is taken",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Boolean answer"),
            }
    )
    @GetMapping("/check/{username}")
    public ResponseEntity<Boolean> isUsernameAlreadyTaken(@PathVariable String username) {
        boolean isTaken = userService.isUsernameAlreadyTaken(username);
        return ResponseEntity.ok(isTaken);
    }
}
