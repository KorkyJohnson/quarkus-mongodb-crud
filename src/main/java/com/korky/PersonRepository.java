package com.korky;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonRepository {

    private final MongoClient mongoClient;
    private final MongoCollection<PersonEntity> collection;

    public PersonRepository(MongoClient mongoClient) {

        this.mongoClient = mongoClient;
        this.collection = mongoClient.getDatabase("test").getCollection("persons", PersonEntity.class);
     
    }

}
