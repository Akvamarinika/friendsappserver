package com.akvamarin.friendsappserver.services;


import com.akvamarin.friendsappserver.domain.entity.data.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileService {
    void saveLocationExcelToDB(MultipartFile file);

}
