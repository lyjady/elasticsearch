package com.example.elasticsearch.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author linyongjin
 * @date 2019/7/2 21:42
 */
@Document(indexName = "item", shards = 1, replicas = 1)
public class Item {

    @Id
    private long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", store = true)
    private String name;

    @Field(type = FieldType.Double, index = true, store = true)
    private double price;

    @Field(type = FieldType.Keyword, store = true)
    private String tag;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", store = true)
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", tag='" + tag + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
