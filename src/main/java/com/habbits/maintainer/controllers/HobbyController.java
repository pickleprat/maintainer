package com.habbits.maintainer.controllers;

import com.habbits.maintainer.models.response.CreateHobbyResponse;
import com.habbits.maintainer.services.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hobby")
public class HobbyController {
    @Autowired
    private CreateHobbyResponse res;

    @Autowired
    private MongoService service;

    @GetMapping("/create")
    public void createHobby() {
        service.getConnection();
    }
}
