package com.example.elasticsearch.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Indexed;

/**
 * @author LinYongJin
 * @date 2019/8/29 19:55
 */
@Document(indexName = "computer", type = "computer", shards = 1, replicas = 0)
public class Computer {

    @Id
    private Long id;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", fielddata = true)
    private String name;

    @Field(type = FieldType.Double, store = true)
    private Double price;

    @Field(type = FieldType.Keyword, store = true)
    private String tag;

    public Computer() {
    }

    public Computer(Long id, String name, Double price, String tag) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tag = tag;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", tag='" + tag + '\'' +
                '}';
    }
}
