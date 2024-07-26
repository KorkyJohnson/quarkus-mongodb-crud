package com.korky;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

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

    public long anniversaryPerson(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        Bson update = inc("age", 1);
        return collection.updateOne(filter, update).getModifiedCount();
    }

    public long deletePerson(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        return collection.deleteOne(filter).getDeletedCount();
    }


}
