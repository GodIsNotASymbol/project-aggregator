package com.example.project_aggregator.repository;

import com.example.project_aggregator.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
