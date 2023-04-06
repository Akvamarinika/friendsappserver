package com.akvamarin.friendsappserver.services.impl;


import com.akvamarin.friendsappserver.domain.entity.data.Image;
import com.akvamarin.friendsappserver.repositories.ImageRepository;
import com.akvamarin.friendsappserver.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public String saveImage(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String directory = "uploads/";
            String filepath = Paths.get(directory, filename).toString();
            Files.copy(file.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image();
            image.setName(filename);
            image.setUrl("/images/" + filename);
            imageRepository.save(image);
            return image.getUrl();
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file.", e);
        }
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public void deleteImage(Long id) {
        Optional<Image> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            String filename = image.getName();
            String directory = "uploads/";
            String filepath = Paths.get(directory, filename).toString();
            try {
                Files.deleteIfExists(Paths.get(filepath));
            } catch (IOException e) {
                throw new RuntimeException("Could not delete the file.", e);
            }
            imageRepository.delete(image);
        } else {
            throw new RuntimeException("Image not found.");
        }
    }
}
