package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.CityDTO;
import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.services.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Slf4j
public class CityRestController {

    private final CityService cityService;

    @Operation(
            summary = "Get all cities",
            responses = @ApiResponse(responseCode = "200",
                    description = "All cities",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDTO.class))))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CityDTO> getAllEvents() {
        return cityService.findAll();
    }
}
