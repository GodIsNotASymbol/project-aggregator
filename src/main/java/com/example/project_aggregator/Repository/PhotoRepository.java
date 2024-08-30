package com.example.project_aggregator.repository;

import com.example.project_aggregator.entity.Item;
import com.example.project_aggregator.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query("SELECT p FROM Photo p WHERE p.item = ?1")
    Photo findByItem(Item item);
}
