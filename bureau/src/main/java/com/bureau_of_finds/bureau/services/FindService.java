package com.bureau_of_finds.bureau.services;

import com.bureau_of_finds.bureau.models.Find;
import com.bureau_of_finds.bureau.models.Image;
import com.bureau_of_finds.bureau.models.User;
import com.bureau_of_finds.bureau.repo.FindRepository;
import com.bureau_of_finds.bureau.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FindService {
    private final UserRepository userRepository;
    private final FindRepository findRepository;


    public void saveFind(Find find, MultipartFile file) throws IOException {
        if (file.getSize() != 0) {
            Image image = toImageEntity(file);
            image.setPreviewImage(true);
            find.addImageToProduct(image);
            find.setImagePath(image.getFilePath());
            log.info("Saving new Product. Title: {}; Author: {}", find.getTitle(), find.getUser());
            find.setPreviewImageId(find.getImage().getId());
            findRepository.save(find);
        }
    }


    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        // Set the file path
        String filePath = "images/" + file.getOriginalFilename();
        image.setFilePath(filePath);


        return image;
    }

}
