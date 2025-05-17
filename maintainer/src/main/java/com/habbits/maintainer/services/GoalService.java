package com.habbits.maintainer.services;

import com.habbits.maintainer.models.entities.Goal;
import com.habbits.maintainer.models.entities.Task;
import com.habbits.maintainer.repository.GoalRepository;
import com.habbits.maintainer.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {
    @Autowired private GoalRepository goalRepository;
    @Autowired private TaskRepository taskRepository;

    public void create(Goal goal) {
        goalRepository.save(goal);
    }

    public List<Goal> getAll() {
        return goalRepository.findAll();
    }

    public Goal findById(ObjectId id) {
        return goalRepository.findById(id).orElse(null);
    }

    public void replace(ObjectId id, Goal newGoal) {
        Goal old = this.findById(id);
        if(old != null) {
            goalRepository.delete(old);
            goalRepository.save(newGoal);
        }
    }

    public void delete(ObjectId id) {
        List<Task> tasks = taskRepository.findAll();
        Goal goal = goalRepository.findById(id).orElse(null);
        if(goal != null) {
            goalRepository.deleteById(id);
            tasks.removeIf(tsk -> !tsk.getGoal().equals(goal));
            taskRepository.deleteAll(tasks);
        }
    }

    public void deleteMany(List<Goal> goals) {
        for(Goal goal: goals) {
            this.delete(goal.getId());
        }
    }
}
