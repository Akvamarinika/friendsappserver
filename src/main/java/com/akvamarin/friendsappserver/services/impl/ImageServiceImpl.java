package com.akvamarin.friendsappserver.services.impl;


import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.data.Image;
import com.akvamarin.friendsappserver.repositories.ImageRepository;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.ImageService;
import com.akvamarin.friendsappserver.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Environment environment;

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void setNewAvatar(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", userId)));

        if (user.getUrlAvatar() != null) {
            String oldAvatarFilePath = user.getUrlAvatar();
            Path oldAvatarPath = Paths.get(oldAvatarFilePath);
            Files.deleteIfExists(oldAvatarPath); // delete old avatar file
        }

        String newAvatarFilename = UUID.randomUUID() + "-" + file.getOriginalFilename(); // new file name

        String newAvatarFilePath = getBaseUrl() + Constants.IMAGE_DIRECTORY_URL + newAvatarFilename; // set new path
        user.setUrlAvatar(newAvatarFilePath);
        userRepository.save(user);

        // save file
        String filepath = Paths.get(Constants.IMAGE_DIRECTORY_ROOT, newAvatarFilename).toString();
        Files.copy(file.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);

        log.info("Method *** setNewAvatar *** : UserDTO = {} Login = {} filepath = {}", user, user.getUsername(), filepath);
    }

    @Override
    @Transactional
    public String saveImage(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String filepath = Paths.get(Constants.IMAGE_DIRECTORY_URL, filename).toString();
            Files.copy(file.getInputStream(), Paths.get(filepath), StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image();
            image.setName(filename);

            String baseUrl = getBaseUrl();
            String imageUrl = baseUrl + Constants.IMAGE_DIRECTORY_URL + filename;
            image.setUrl(imageUrl);
            imageRepository.save(image);
            return imageUrl;

        } catch (IOException e) {
            throw new RuntimeException("Could not store the file.", e);
        }
    }


    @Override
    @Transactional
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteImage(Long id) {
        Optional<Image> imageOptional = imageRepository.findById(id);

        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            String filename = image.getName();
            String filepath = Paths.get(Constants.IMAGE_DIRECTORY_URL, filename).toString();

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

    private String getBaseUrl() {
        String address = environment.getProperty("server.address");
        String port = environment.getProperty("server.port");
        return "http://" + address + ":" + port;
    }

}
