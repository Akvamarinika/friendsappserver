package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.entity.data.Image;
import com.akvamarin.friendsappserver.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageRestController {

    private final ImageService imageService;

    @SneakyThrows
    @PostMapping("/{userId}/avatar")
    public ResponseEntity<Void> setAvatar(@RequestParam("file") MultipartFile file,
                                          @PathVariable Long userId) {
        imageService.setNewAvatar(file, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(name = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = imageService.saveImage(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}