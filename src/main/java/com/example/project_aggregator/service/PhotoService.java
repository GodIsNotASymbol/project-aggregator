package com.example.project_aggregator.service;

import com.example.project_aggregator.entity.Item;
import com.example.project_aggregator.entity.Photo;
import com.example.project_aggregator.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class PhotoService {


    private final String photoFolderPath = "./uploads/photos/";

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository){
        this.photoRepository = photoRepository;
    }

    public Photo retrievePhotoForItem(Item item){
        Photo photo = this.photoRepository.findByItem(item);
        return photo;
    }

    public String retrievePhotoBase64ForItem(Item item){
        /* ONLY TO PRINT THE CURRENT DIRECTORY
        String currentDirectory = System.getProperty("user.dir");
        // Print the current working directory
        System.out.println("Current working directory: " + currentDirectory);
        */
        String prefix = "data:image/jpeg;base64,";
        Photo photo = this.photoRepository.findByItem(item);
        String filename = photo.getFilename();
        File imgFile = new File(photoFolderPath+filename);
        try {
            byte[] imageBytes = Files.readAllBytes(imgFile.toPath());
            String base64Image = Base64Utils.encodeToString(imageBytes);
            return prefix+base64Image;
        } catch (IOException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
