package com.example.project_aggregator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "photo", schema = "project_aggregator")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "filename")
    String filename;

    @Column(name = "timestamp_created")
    String timestamp_created;

    @Column(name = "timestamp_edited")
    String timestamp_edited;

    @Column(name = "timestamp_deleted")
    String timestamp_deleted;

    @ManyToOne(targetEntity = Item.class, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;
}
