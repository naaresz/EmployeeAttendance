package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static final String URI = "mongodb://localhost:27017";
    
    public static MongoDatabase getDatabase(){
        MongoClient client = MongoClients.create(URI);
        MongoDatabase database = client.getDatabase("employee_db");
        
        System.out.println("Connected to MangoDB!");
        return database;
    }
}
