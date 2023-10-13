package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import java.io.*;

import static com.mongodb.client.model.Filters.eq;

public class binaryData {
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
        collection=database.getCollection("image");
    }
    public static void close()
    {
        mongoClient.close();
    }

    public static void main(String[] args) throws IOException
    {
        connect();
       // writeBinary();
        downloadBinary();
        close();
    }

    public static void writeBinary(){
        // read media file into byte array
        File file = new File("pic1.jpg");
        byte[] mediaData = new byte[(int) file.length()];
        try (FileInputStream stream = new FileInputStream(file)) {
            stream.read(mediaData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // create a document and add media file
        Document document = new Document();
        document.put("mediaData", mediaData);

        // insert the document into the collection
        collection.insertOne(document);
    }

    public static void downloadBinary() throws IOException {

        // Query filter
        Bson filter = Filters.exists("mediaData",true);
        Document document = collection.find(filter).first();

        // Get binary data
        assert document != null;
        Binary binary = document.get("mediaData", Binary.class);

        //Convert binary data to array
        byte[] bytes = binary.getData();

        FileOutputStream outputImage = new FileOutputStream("output.jpg");
        outputImage.write(bytes);
        outputImage.close();
    }
}
