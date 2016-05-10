package MongoHandler;

import com.mongodb.Block;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import org.bson.Document;

public class MongoHandler {
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    public MongoHandler(){
        init();
        //test();
        //close();
    }

    private void init(){
        this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        this.mongoDB = mongoClient.getDatabase("testDB");
    }

    public void close(){
        this.mongoClient.close();
    }

    public void test(){
        FindIterable<Document> iterable = mongoDB.getCollection("testColl").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });

/*
            Document obj = new Document("title", "test1")
                    .append("asd", "cda");
            db.getCollection("testColl").insertOne(obj);*/
    }
}
