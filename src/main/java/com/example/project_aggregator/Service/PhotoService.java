package com.example.project_aggregator.Service;

import com.example.project_aggregator.Entity.Item;
import com.example.project_aggregator.Entity.Photo;
import com.example.project_aggregator.Repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

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

    public String saveImage(MultipartFile image, Item item){
        Photo photo = new Photo();
        photo.setItem(item);
        photo.setFilename(image.getOriginalFilename());
        photo.setTimestamp_created(new Date());
        photo.setTimestamp_edited(new Date());
        photoRepository.save(photo);

        if (!image.isEmpty()) {
            try {
                // Ensure the directory exists
                Path uploadPath = Paths.get(photoFolderPath);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the image to the filesystem
                String imageName = image.getOriginalFilename();
                Path filePath = uploadPath.resolve(imageName);
                Files.write(filePath, image.getBytes());

                System.out.println("Uploaded image saved to: " + filePath.toString());

                return "Item created successfully! Image saved to " + filePath.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to save the image!";
            }
        }
        return "Image is empty";
    }
}
