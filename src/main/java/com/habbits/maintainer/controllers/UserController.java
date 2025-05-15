package com.habbits.maintainer.controllers;

import com.habbits.maintainer.models.entities.User;
import com.habbits.maintainer.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService service;

    public boolean userNameExists(String userName) {
        List<User> users = service.getAll();
        if(users != null && !users.isEmpty()) {
            for(User existingUser: users) {
                if(existingUser.getUserName().strip().toLowerCase()
                        .equals(userName.toLowerCase().strip())) {
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> create(@RequestBody User user) {
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(null);
        boolean exists = userNameExists(user.getUserName());
        if(!exists) {
            service.create(user);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(false, HttpStatus.CONFLICT);
    }

    @GetMapping("/view")
    public ResponseEntity<List<User>> view() {
        List<User> users = service.getAll();
        if(users != null && !users.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/filter/id/{id}")
    public ResponseEntity<User> filter(@PathVariable ObjectId id) {
        User user = service.findById(id);
        if(user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/filter/userName/{userName}")
    public ResponseEntity<User> filter(@PathVariable String userName) {
        List<User> users = service.getAll();
        for(User user : users) {
            if(user.getUserName().equals(userName)) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<Void> editById(@PathVariable ObjectId id, @RequestBody User user) {
        User old = service.findById(id);
        if(old != null && user.getUserName().equals(old.getUserName())) {
            old.setPassword(user.getPassword());
            old.setAddress(user.getAddress());
            old.setZipCode(user.getZipCode());
            old.setCity(user.getCity());
            old.setMiddleName(user.getMiddleName());
            old.setUpdated_at(LocalDateTime.now());
            old.setFirstName(user.getFirstName());
            old.setLastName(user.getLastName());
            old.setCountryCode(user.getCountryCode());
            old.setPhoneNumber(user.getPhoneNumber());
            service.replace(id, old);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if(old == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update/userName/{userName}")
    public ResponseEntity<Void> editById(@PathVariable String userName, @RequestBody User user) {
        List<User> users = service.getAll();
        for(User old: users) {
            if(old.getUserName().equals(userName)) {
                old.setPassword(user.getPassword());
                old.setAddress(user.getAddress());
                old.setZipCode(user.getZipCode());
                old.setCity(user.getCity());
                old.setMiddleName(user.getMiddleName());
                old.setUpdated_at(LocalDateTime.now());
                old.setFirstName(user.getFirstName());
                old.setLastName(user.getLastName());
                old.setCountryCode(user.getCountryCode());
                old.setPhoneNumber(user.getPhoneNumber());
                service.replace(old.getId(), old);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable ObjectId id) {
       User user = service.findById(id);
       if(user != null) {
           service.delete(id);
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/userName/{userName}")
    public ResponseEntity<Void> delete(@PathVariable String userName) {
        List<User> users = service.getAll();
        for(User user: users) {
            if(user.getUserName().equals(userName)) {
                service.delete(user.getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
