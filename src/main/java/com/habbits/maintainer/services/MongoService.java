package com.habbits.maintainer.services;

import com.habbits.maintainer.repository.DBRepository;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MongoService implements DBRepository<MongoDatabase> {

    @Value("${spring.data.mongodb.uri}")
    private String connString;

    @Value("${spring.data.mongodb.database}")
    private String database;

    public MongoDatabase getConnection() throws MongoException {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connString))
                .serverApi(serverApi)
                .build();

        try(MongoClient mongoClient = MongoClients.create(settings))  {
            return mongoClient.getDatabase(database);
        }
    }

//    public boolean insertInto(String collectionName, )
}
