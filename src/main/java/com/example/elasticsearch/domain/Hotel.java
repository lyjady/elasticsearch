package com.example.elasticsearch.domain;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

/**
 * @author LinYongJin
 * @date 2019/9/1 13:19
 */
@Document(indexName = "hotel", type = "hotel", shards = 1, replicas = 0)
public class Hotel {

    @Id
    private Long id;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word", fielddata = true, searchAnalyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word", fielddata = true, searchAnalyzer = "ik_max_word")
    private String description;

    @GeoPointField
    private GeoPoint position;

    public Hotel() {

    }

    public Hotel(Long id, String name, String description, GeoPoint position) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeoPoint getPosition() {
        return position;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", position=" + position +
                '}';
    }
}
