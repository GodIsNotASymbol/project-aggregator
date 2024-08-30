package com.example.project_aggregator.Repository;

import com.example.project_aggregator.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query(value = "SELECT i FROM Item i")
    public List<Item> selectAll();

}
