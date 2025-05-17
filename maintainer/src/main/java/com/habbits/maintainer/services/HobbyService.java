package com.habbits.maintainer.services;

import com.habbits.maintainer.models.entities.Goal;
import com.habbits.maintainer.models.entities.Hobby;
import com.habbits.maintainer.repository.GoalRepository;
import com.habbits.maintainer.repository.HobbyRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HobbyService {
    @Autowired private HobbyRepository hobbyRepository;
    @Autowired private GoalRepository goalRepository;

    public void create(Hobby hobby) {
        hobbyRepository.save(hobby);
    }

    public List<Hobby> getAll() {
        return hobbyRepository.findAll();
    }

    public Hobby findById(ObjectId id) {
        return hobbyRepository.findById(id).orElse(null);
    }

    public void replace(ObjectId id, Hobby newHobby) {
        Hobby old = this.findById(id);
        if(old != null) {
            hobbyRepository.delete(old);
            hobbyRepository.save(newHobby);
        }
    }

    public void delete(ObjectId id) {
        Hobby hobby = hobbyRepository.findById(id).orElse(null);
        List<Goal> goals = goalRepository.findAll();
        if(hobby != null){
            hobbyRepository.deleteById(id);
            goals.removeIf(gl -> !gl.getHobby().equals(hobby));
            goalRepository.deleteAll(goals);
        }
    }
}
