package com.habbits.maintainer.models.response;

import org.springframework.stereotype.Component;

@Component
public class CreateHobbyResponse {
    private int id;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
