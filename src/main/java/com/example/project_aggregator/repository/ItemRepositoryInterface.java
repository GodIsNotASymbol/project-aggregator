package com.example.project_aggregator.repository;

import com.example.project_aggregator.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepositoryInterface extends JpaRepository<Item, Integer> {

    @Query(value = "SELECT i FROM Item i")
    public List<Item> selectAll();

}
