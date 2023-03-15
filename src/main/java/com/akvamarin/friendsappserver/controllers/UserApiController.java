package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.services.UserService;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.responseerror.ErrorResponse;
import com.akvamarin.friendsappserver.domain.responseerror.ValidationErrorResponse;
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

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

  /*  @GetMapping("/getUser")
    public User getUser(@RequestBody User user) {
        return userService.getUser(user);
    }*/


    @Operation(
            summary = "Create new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "New user is created"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(name = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {


       // if (userDTO.getPassword().isEmpty() || userDTO.getEmail().isEmpty() || userDTO.getNickname().isEmpty()) {
      //      return new ResponseEntity<>("Пожалуйста, заполните все поля!", HttpStatus.BAD_REQUEST);
       // }

        final User user = userService.createNewUser(userDTO);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uri).build();

        //return userService.createNewUser(userDTO);
    }

    @Operation(
            summary = "Get all users",
            responses = @ApiResponse(responseCode = "200",
                    description = "All users",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    @Operation(
            summary = "Find user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User by ID found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUserByID(@PathVariable Long id) {
        return userService.findById(id);
    }

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


    @Operation(
            summary = "Remove user by ID",
            responses = @ApiResponse(responseCode = "204", description = "User for requested ID is removed")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }


    @PostMapping("/clientSendToken")
    public String sendToken(@RequestBody String token) {
        //userService.
        return "1111111111";
    } //@RequestBody в теле запроса отправляет клиент
}
