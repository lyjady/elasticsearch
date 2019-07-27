package com.example.elasticsearch.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author linyongjin
 * @date 2019/7/25 20:03
 */
@Document(indexName = "pro", type = "phone", shards = 1, replicas = 0)
public class Phone {

    @Id
    private long id;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Double, store = true)
    private double price;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word")
    private String tag;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_max_word")
    private String contents;

    public Phone() {

    }

    public Phone(long id, String name, double price, String tag, String contents) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tag = tag;
        this.contents = contents;
    }

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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", tag='" + tag + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
