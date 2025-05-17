package com.habbits.maintainer.controllers;

import com.habbits.maintainer.models.entities.Goal;
import com.habbits.maintainer.models.entities.Hobby;
import com.habbits.maintainer.models.entities.User;
import com.habbits.maintainer.services.GoalService;
import com.habbits.maintainer.services.HobbyService;
import com.habbits.maintainer.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    @Autowired private GoalService goalService;
    @Autowired private UserService userService;
    @Autowired private HobbyService hobbyService;

    public boolean goalExists(User user, Hobby hobby){
        List<Goal> goals = goalService.getAll();
        if(goals != null && !goals.isEmpty()) {
            for(Goal goal: goals){
                if(goal.getUser().equals(user) && goal.getHobby().equals(hobby)){
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/create/{userId}/{hobbyId}")
    public ResponseEntity<Boolean> create(
            @PathVariable ObjectId userId,
            @PathVariable ObjectId hobbyId,
            @RequestBody Goal goal
    ) {
        User user = userService.findById(userId);
        Hobby hobby = hobbyService.findById(hobbyId);
        if(user != null && hobby != null && !this.goalExists(user, hobby)) {
            goal.setCreated_at(LocalDateTime.now());
            goal.setHobby(hobby);
            goal.setUser(user);
            goalService.create(goal);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    };

    @GetMapping("/filter/user/{userId}")
    public ResponseEntity<List<Goal>> filterUser(@PathVariable ObjectId userId) {
        User user = userService.findById(userId);
        List<Goal> goals = goalService.getAll();
        if(user != null && goals != null && !goals.isEmpty()) {
            goals.removeIf(gls -> !gls.getUser().equals(user));
            return new ResponseEntity<>(goals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/filter/hobby/{hobbyId}")
    public ResponseEntity<List<Goal>> filterHobby(@PathVariable ObjectId hobbyId) {
        Hobby hobby = hobbyService.findById(hobbyId);
        List<Goal> goals = goalService.getAll();
        if(hobby != null && goals != null && !goals.isEmpty()) {
            goals.removeIf(gls -> !gls.getHobby().equals(hobby));
            return new ResponseEntity<>(goals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/view")
    public ResponseEntity<List<Goal>> show() {
        return new ResponseEntity<>(goalService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/update/id/{id}")
    public ResponseEntity<Void> update(@PathVariable ObjectId id, @RequestBody Goal goal) {
        Goal old = goalService.findById(id);
        if(old != null) {
            old.setObjective(goal.getObjective());
            old.setUnit(goal.getUnit());
            old.setSteps(goal.getSteps());
            old.setUpdated_at(LocalDateTime.now());
            goalService.replace(id, old);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable ObjectId id) {
        if(goalService.findById(id) != null)  {
            goalService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
