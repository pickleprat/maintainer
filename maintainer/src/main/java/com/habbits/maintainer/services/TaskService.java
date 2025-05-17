package com.habbits.maintainer.services;

import com.habbits.maintainer.models.entities.Task;
import com.habbits.maintainer.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired private TaskRepository repository;

    public void create(Task task) {
        repository.save(task);
    }

    public List<Task> getAll() {
        return repository.findAll();
    }

    public Task findById(ObjectId id) {
        return repository.findById(id).orElse(null);
    }

    public void replace(ObjectId id, Task task) {
        Task old = this.findById(id);
        if(old != null) {
            repository.delete(old);
            repository.save(task);
        }
    }

    public void delete(ObjectId id) {
        repository.deleteById(id);
    }
}
