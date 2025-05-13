package com.habbits.maintainer.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="hobbys")
public class Hobbys {

    @Id
    private String id;

    private String title;
    private String description;
}
