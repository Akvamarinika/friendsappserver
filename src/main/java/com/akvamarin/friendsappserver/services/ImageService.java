package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.entity.data.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    String saveImage(MultipartFile file);

    List<Image> getAllImages();

    void deleteImage(Long id);

    void setNewAvatar(MultipartFile file, Long userId) throws IOException;
}
