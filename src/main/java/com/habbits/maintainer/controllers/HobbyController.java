package com.habbits.maintainer.controllers;

import com.habbits.maintainer.models.entities.Hobbys;
import com.habbits.maintainer.services.HobbyService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/hobby")
public class HobbyController {
    @Autowired
    private HobbyService service;

    private boolean hobbyExists(Hobbys hobby) {
        List<Hobbys> hobbyList = service.getAll();
        hobby.setTitle(hobby.getTitle().toLowerCase().strip());
        hobby.setCategory(hobby.getCategory().toLowerCase().strip());

        boolean exists = false;
        for(Hobbys existingHobby: hobbyList) {
            if(existingHobby.getTitle().toLowerCase().strip().equals(hobby.getTitle().toLowerCase().strip())) {
                exists = true;
                break;
            }
        }

        return exists;
    }

    @PostMapping("/create")
    public boolean createHobby(@RequestBody Hobbys hobby) {
        boolean exists = this.hobbyExists(hobby);
        if(!exists) {
            hobby.setCreated_at(LocalDateTime.now());
            hobby.setUpdated_at(null);
            service.create(hobby);
        }
        // if it exists then we didn't add it, if it doesn't, then we did.
        return !exists;
    }

    @GetMapping("/view")
    public List<Hobbys> viewHobbies() {
        return service.getAll();
    }

    @PutMapping("/update/{id}")
    public Hobbys update(@PathVariable ObjectId id, @RequestBody Hobbys newHobby) {
        Hobbys prev = service.findById(id);
        if(prev != null && prev.getTitle().equals(newHobby.getTitle().toLowerCase().strip())) {
            prev.setDescription(newHobby.getDescription().toLowerCase().strip());
            prev.setUpdated_at(LocalDateTime.now());
            service.replace(prev.getId(), prev);
        }

        return prev;
    }

    @DeleteMapping("/delete/id/{id}")
    public boolean delete(@PathVariable ObjectId id) {
        Hobbys hb = service.findById(id);
        if(hb != null) {
            service.delete(id);
            return true;
        }
        return false;
    }

    @DeleteMapping("/delete/title/{title}")
    public boolean delete(@PathVariable String title) {
        List<Hobbys> allHobbys = service.getAll();
        for(Hobbys hb : allHobbys) {
            if(hb.getTitle().equals(title.strip().toLowerCase())){
                service.delete(hb.getId());
                return true;
            }
        }
        return false;
    }

    @GetMapping("/filter/{category}")
    public List<Hobbys> getHobbyByCategory(@PathVariable String category) {
        List<Hobbys> allHobbys = service.getAll();
        allHobbys.removeIf(hb ->
                !hb.getCategory().toLowerCase().strip()
                        .equals(category.toLowerCase().strip()));
        return allHobbys;
    }
}
