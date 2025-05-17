package com.habbits.maintainer.controllers;

import com.habbits.maintainer.models.entities.Goal;
import com.habbits.maintainer.models.entities.Period;
import com.habbits.maintainer.models.entities.Task;
import com.habbits.maintainer.services.GoalService;
import com.habbits.maintainer.services.TaskService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired private TaskService taskService;
    @Autowired private GoalService goalService;

    private boolean taskExists(Task task) {
        List<Task> taskList = taskService.getAll();
        boolean exists = false;
        for(Task existingTask: taskList) {
            if(existingTask.getTaskName().toLowerCase().strip()
                    .equals(task.getTaskName().toLowerCase().strip())) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    @PostMapping("/create/{goalId}")
    public ResponseEntity<Boolean> create(@PathVariable ObjectId goalId, @RequestBody Task task) {
        boolean exists = this.taskExists(task);
        task.setTaskName(task.getTaskName().toLowerCase().strip());
        Goal goal = goalService.findById(goalId);
        if(!exists && goal != null) {
            task.setCreated_at(LocalDateTime.now());
            task.setUpdated_at(null);
            task.setGoal(goal);
            taskService.create(task);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }

        // if it exists then we didn't add it, if it doesn't, then we did.
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(false);
    }

    @GetMapping("/view")
    public ResponseEntity<List<Task>> view() {
        List<Task> users = taskService.getAll();
        if(users != null && !users.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/filter/duration/{duration}")
    public ResponseEntity<List<Task>> filter(@PathVariable Period duration) {
        List<Task> tasks = taskService.getAll();
        if(tasks != null && !tasks.isEmpty()) {
            tasks.removeIf(tk -> tk.getDuration() != duration);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/filter/taskName/{taskName}")
    public ResponseEntity<List<Task>> filter(@PathVariable String taskName) {
        List<Task> tasks = taskService.getAll();
        if(tasks != null && !tasks.isEmpty()) {
            tasks.removeIf(tk -> !tk.getTaskName().toLowerCase().strip()
                        .equals(taskName.toLowerCase().strip()));
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<Void> update(@PathVariable ObjectId id, @RequestBody Task task) {
        Task old = taskService.findById(id);
        if(old != null && old.getTaskName().toLowerCase().strip()
                .equals(task.getTaskName().toLowerCase().strip())) {
            old.setDescription(task.getDescription());
            old.setUpdated_at(LocalDateTime.now());
            old.setDuration(task.getDuration());
            old.setRepetition(task.getRepetition());
            taskService.replace(old.getId(), old);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if(old == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @PutMapping("/update/taskName/{taskName}")
    public ResponseEntity<Void> update(@PathVariable String taskName, @RequestBody Task task) {
        List<Task> tasks = taskService.getAll();
        if(tasks != null && !tasks.isEmpty()) {
            for(Task exTask: tasks) {
                if(exTask.getTaskName().toLowerCase().strip()
                        .equals(task.getTaskName().toLowerCase().strip())){
                    exTask.setDescription(task.getDescription());
                    exTask.setUpdated_at(LocalDateTime.now());
                    exTask.setDuration(task.getDuration());
                    exTask.setRepetition(task.getRepetition());
                    taskService.replace(exTask.getId(), exTask);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable ObjectId id) {
        Task task = taskService.findById(id);
        if(task != null) {
            taskService.delete(id);
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/taskName/{taskName}")
    public ResponseEntity<Boolean> delete(@PathVariable String taskName) {
        List<Task> tasks = taskService.getAll();
        if(tasks != null && !tasks.isEmpty()) {
            for(Task tsk : tasks) {
                if(tsk.getTaskName().toLowerCase().strip()
                        .equals(taskName.toLowerCase().strip())) {
                    taskService.delete(tsk.getId());
                    return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
                }
            }
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
