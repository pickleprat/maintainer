package com.habbits.maintainer.controllers;

import com.habbits.maintainer.models.entities.Hobbys;
import com.habbits.maintainer.services.HobbyService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 201 OK       : If resource has been created
    // 409 Conflict : If resource already exists
    @PostMapping("/create")
    public ResponseEntity<Boolean> createHobby(@RequestBody Hobbys hobby) {
        boolean exists = this.hobbyExists(hobby);
        if(!exists) {
            hobby.setCreated_at(LocalDateTime.now());
            hobby.setUpdated_at(null);
            service.create(hobby);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }

        // if it exists then we didn't add it, if it doesn't, then we did.
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(false);
    }

    // 200 OK        : If all hobbies are accessible
    // 404 Not Found : If hobbies are non-existent
    @GetMapping("/view")
    public ResponseEntity<List<Hobbys>> viewHobbies() {
        List<Hobbys> all = service.getAll();
        if(all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK) ;
        }

        return new ResponseEntity<>(all, HttpStatus.NOT_FOUND);
    }

    // 204 No Content : No content was to be returned from this
    // 404 Not found  : The resource you tried to update doesn't exist
    // 403 Forbidden  : The update you tried to make to the resource is not allowed.
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable ObjectId id, @RequestBody Hobbys newHobby) {
        Hobbys prev = service.findById(id);
        if(prev != null && prev.getTitle().equals(newHobby.getTitle().toLowerCase().strip())) {
            prev.setDescription(newHobby.getDescription().toLowerCase().strip());
            prev.setUpdated_at(LocalDateTime.now());
            service.replace(prev.getId(), prev);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if(prev == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    // 204 No Content: the resource has been deleted sucessfully.
    // 404 Not found : the resource you tried to delete does not exist.
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable ObjectId id) {
        Hobbys hb = service.findById(id);
        if(hb != null) {
            service.delete(id);
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    // 204 No Content: the resource has been deleted sucessfully.
    // 404 Not found : the resource you tried to delete does not exist.
    @DeleteMapping("/delete/title/{title}")
    public ResponseEntity<Boolean> delete(@PathVariable String title) {
        List<Hobbys> allHobbys = service.getAll();
        for(Hobbys hb : allHobbys) {
            if(hb.getTitle().equals(title.strip().toLowerCase())){
                service.delete(hb.getId());
                return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
            }
        }

        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    // 200 OK       : the filter is not empty
    // 404 Not Found: the filter rows do not exist
    @GetMapping("/filter/{category}")
    public ResponseEntity<List<Hobbys>> getHobbyByCategory(@PathVariable String category) {
        List<Hobbys> allHobbys = service.getAll();
        if(allHobbys == null || allHobbys.isEmpty()) {
            return new ResponseEntity<>(allHobbys, HttpStatus.NOT_FOUND);
        }

        allHobbys.removeIf(hb ->
                !hb.getCategory().toLowerCase().strip()
                        .equals(category.toLowerCase().strip()));

        if (allHobbys.isEmpty()) {
            return new ResponseEntity<>(allHobbys, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(allHobbys, HttpStatus.OK);
    }
}
