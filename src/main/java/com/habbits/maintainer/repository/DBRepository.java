package com.habbits.maintainer.repository;

import com.mongodb.client.MongoDatabase;

public interface DBRepository<T> {
    public T getConnection();
}
