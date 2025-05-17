package com.habbits.maintainer.repository;

import com.habbits.maintainer.models.entities.Hobby;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HobbyRepository extends MongoRepository<Hobby, ObjectId> { }
