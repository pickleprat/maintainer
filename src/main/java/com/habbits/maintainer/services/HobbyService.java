package com.habbits.maintainer.services;

import com.habbits.maintainer.models.entities.Hobby;
import com.habbits.maintainer.repository.HobbyRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HobbyService {
    @Autowired
    private HobbyRepository repository;

    public void create(Hobby hobby) {
        repository.save(hobby);
    }

    public List<Hobby> getAll() {
        return repository.findAll();
    }

    public Hobby findById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    public void replace(ObjectId id, Hobby newHobby) {
        Hobby old = this.findById(id);
        if(old != null) {
            repository.delete(old);
            repository.save(newHobby);
        }
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }
}
