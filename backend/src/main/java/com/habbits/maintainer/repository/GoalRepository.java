package com.habbits.maintainer.repository;

import com.habbits.maintainer.models.entities.Goal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GoalRepository extends MongoRepository<Goal, ObjectId> { }
