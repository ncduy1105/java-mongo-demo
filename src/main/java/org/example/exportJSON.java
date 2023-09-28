package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class exportJSON{

    static MongoClient mongoClient;
    static MongoCollection<Document> collection;
    static MongoDatabase database;

    public static void connect()
    {
        //create connection
        String uri="mongodb://localhost:27017";
        mongoClient= MongoClients.create(uri);
        //get db
        database = mongoClient.getDatabase("users");
        //get collection
        collection=database.getCollection("student");
    }
    public static void close()
    {
        mongoClient.close();
    }
    public static void main(String[] args) throws IOException
    {
        connect();
        exportJSON();
        close();
    }
    public static void exportJSON() throws IOException {
        // Get all collection names
        String a="";
        for (String collectionName : database.listCollectionNames()) {
            // Get collection
            MongoCollection<Document> collection = database.getCollection(collectionName);
            // Export collection to JSON file
            FileWriter fileWriter = new FileWriter(collectionName + ".json");
            for (Document document : collection.find()) {
                a=a + document.toJson()+","+"\n";
            }
            fileWriter.write("["+a +"]");
            fileWriter.close();
        }
    }
}
