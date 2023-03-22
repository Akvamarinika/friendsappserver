package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.responseerror.ValidationErrorResponse;
import com.akvamarin.friendsappserver.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileRestController {
    private final FileService fileService;

    @Operation(
            summary = "Upload excel-location file",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Excel file is upload"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(name = "/upload-cities-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> uploadCitiesData(@RequestParam("file")MultipartFile file){
        fileService.saveLocationExcelToDB(file);
        return ResponseEntity
                .ok(Map.of("Message" , " Customers data uploaded and saved to database successfully"));
    }

}
