package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class importJSON {
    static MongoClient mongoClient;
    static MongoCollection<Document> collection;
    public static void connect()
    {
        //create connection
        String uri="mongodb://localhost:27017";
        mongoClient= MongoClients.create(uri);
        //get db
        MongoDatabase database = mongoClient.getDatabase("users");
        //get collection
        collection=database.getCollection("student");
    }
    public static void close()
    {
        //close connection
        mongoClient.close();
    }


    public static void main(String[] args) throws IOException {
        connect();
        //insertFromJSON();
        findID();
        findName();
        countDegrees();
        countCountry();
        close();
    }

    public static void insertFromJSON() throws IOException {
        //read JSON file
        String filePath="teachers.json";
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> records = objectMapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
        List<Document> documents = new ArrayList<Document>();
        for (Map<String, Object> record : records) {
            Document document = new Document(record);
            documents.add(document);
        }

        //insert from JSON file
        collection.insertMany(documents);
    }

    public static void findID()
    {
        String itemId = "650954a236d5d70c008d412c";
        Document item = collection.find(Filters.eq("_id", new ObjectId(itemId))).first();
        System.out.print(item);
    }

    public static void findName()
    {
        String itemName = "Cao";
        Document query = new Document("name", itemName);
        FindIterable<Document> result = collection.find(query);
        for (Document document : result) {
            System.out.println(document.toJson());
        }
    }
    public static void countDegrees()
    {
        String value="master";
        Bson filter = Filters.eq("degrees.type", value);
        long count = collection.countDocuments(filter);
        System.out.println("Number of student with the "+ value+" degree : " + count);
    }

    public static void countCountry()
    {
        String value="America";
        Bson filter = Filters.eq("degrees.country", value);
        long count = collection.countDocuments(filter);
        System.out.println("Number of student with the "+value+" degree : " + count);
    }
}
