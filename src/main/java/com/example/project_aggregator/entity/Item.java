package com.example.project_aggregator.entity;

import jakarta.persistence.Entity;

import java.util.Date;

@Entity
public class Item {
    private Long id;

    private String description;

    private Date created_date;

    private Date last_edit_date;

    private Date deleted_date;

}
