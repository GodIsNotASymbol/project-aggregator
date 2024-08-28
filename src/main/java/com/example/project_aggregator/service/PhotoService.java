package com.example.project_aggregator.service;

import com.example.project_aggregator.entity.Item;
import com.example.project_aggregator.entity.Photo;
import com.example.project_aggregator.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository){
        this.photoRepository = photoRepository;
    }

    public Photo retrievePhotoForItem(Item item){
        Photo photo = this.photoRepository.findByItem(item);
        return photo;
    }
}
