package com.habbits.maintainer.repository;

import com.habbits.maintainer.models.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> { }
