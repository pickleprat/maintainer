package com.habbits.maintainer.services;

import com.habbits.maintainer.models.entities.Hobby;
import com.habbits.maintainer.models.entities.User;
import com.habbits.maintainer.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository repository;

    public void create(User user) {
        repository.save(user);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User findById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    public void replace(ObjectId id, User user) {
        User old = this.findById(id);
        if(old != null) {
            repository.delete(old);
            repository.save(user);
        }
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }
}
