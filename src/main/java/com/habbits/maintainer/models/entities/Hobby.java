package com.habbits.maintainer.models.entities;

import org.springframework.stereotype.Component;

@Component
public class Hobby {
    private int id;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
