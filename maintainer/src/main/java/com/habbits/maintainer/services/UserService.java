package com.habbits.maintainer.services;

import com.habbits.maintainer.models.entities.Goal;
import com.habbits.maintainer.models.entities.User;
import com.habbits.maintainer.repository.GoalRepository;
import com.habbits.maintainer.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private GoalRepository goalRepository;

    public void create(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findById(ObjectId id) {
        return userRepository.findById(id).orElse(null);
    }

    public void replace(ObjectId id, User user) {
        User old = this.findById(id);
        if(old != null) {
            userRepository.delete(old);
            userRepository.save(user);
        }
    }

    public void delete(ObjectId id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            userRepository.deleteById(id);
            List<Goal> goals = goalRepository.findAll();
            goals.removeIf(gl -> !gl.getUser().equals(user));
            goalRepository.deleteAll(goals);
        }
    }
}
