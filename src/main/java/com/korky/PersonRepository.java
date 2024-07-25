package com.korky;

import java.util.ArrayList;
import java.util.List;

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

    public String add(PersonEntity person) {
        return collection.insertOne(person).getInsertedId().asObjectId().getValue().toHexString();
    }

    public List<PersonEntity> getPersons() {
        return collection.find().into(new ArrayList<>());
    }

}
