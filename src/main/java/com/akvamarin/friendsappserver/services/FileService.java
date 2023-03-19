package com.akvamarin.friendsappserver.services;


import org.springframework.web.multipart.MultipartFile;


public interface FileService {
    void saveLocationExcelToDB(MultipartFile file);
}
