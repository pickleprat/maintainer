package com.habbits.maintainer.repository;

import com.habbits.maintainer.models.entities.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, ObjectId> { }
