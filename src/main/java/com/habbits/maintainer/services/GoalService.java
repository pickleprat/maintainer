package com.habbits.maintainer.services;

import com.habbits.maintainer.models.entities.Goal;
import com.habbits.maintainer.models.entities.Goal;
import com.habbits.maintainer.repository.GoalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {
    @Autowired private GoalRepository repository;

    public void create(Goal goal) {
        repository.save(goal);
    }

    public List<Goal> getAll() {
        return repository.findAll();
    }

    public Goal findById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    public void replace(ObjectId id, Goal newGoal) {
        Goal old = this.findById(id);
        if(old != null) {
            repository.delete(old);
            repository.save(newGoal);
        }
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }

    public void deleteMany(List<Goal> goals) {
        repository.deleteAll(goals);
    }
}
