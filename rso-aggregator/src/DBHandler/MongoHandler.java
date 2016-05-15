package DBHandler;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

//db.createUser({"user": "pm", "pwd": "pass123", roles: ["readWrite", "dbAdmin"]})
public class MongoHandler {
    private static String user = "pm";
    private static char[] pass = "pass123".toCharArray();
    private static String dbName = "testDB";
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    public MongoHandler(){
        init();
        //test();
        //close();
    }

    private void init(){
        MongoCredential cred = MongoCredential.createCredential(user, dbName, pass);
        //this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        this.mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(cred));
        this.mongoDB = mongoClient.getDatabase(dbName);
    }

    public void close(){
        this.mongoClient.close();
    }

    public void test(){
        FindIterable<Document> iterable = mongoDB.getCollection("testColl").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                String temp = (String)document.get("title");
                System.out.println(temp);

            }
        });

        /*Document obj = new Document("title", "test1")
                .append("asd", "cda");
        mongoDB.getCollection("testColl").insertOne(obj);*/
    }
}
