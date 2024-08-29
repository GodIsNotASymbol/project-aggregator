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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTimestamp_created() {
        return timestamp_created;
    }

    public void setTimestamp_created(String timestamp_created) {
        this.timestamp_created = timestamp_created;
    }

    public String getTimestamp_edited() {
        return timestamp_edited;
    }

    public void setTimestamp_edited(String timestamp_edited) {
        this.timestamp_edited = timestamp_edited;
    }

    public String getTimestamp_deleted() {
        return timestamp_deleted;
    }

    public void setTimestamp_deleted(String timestamp_deleted) {
        this.timestamp_deleted = timestamp_deleted;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
