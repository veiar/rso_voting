package DBHandler;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


//db.createUser({"user": "pm", "pwd": "pass123", roles: ["readWrite", "dbAdmin"]})
public class MongoHandler {
    private String user;// = "pm";
    private char[] pass;// = "pass123".toCharArray();
    private String dbName;// = "testDB";
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    Map<String, List<String>> propertiesMap;
    public MongoHandler(){
        init();
        //test();
        //close();
    }

    private void init(){
        try {
            getProperties();
        }
        catch (IOException e){
            System.err.println("Loading from cfg file failed, exiting!");
            System.exit(1);
        }
        MongoCredential cred = MongoCredential.createCredential(user, dbName, pass);
        //this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        this.mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(cred));
        this.mongoDB = mongoClient.getDatabase(dbName);
    }

    private void getProperties() throws IOException{
        java.util.Properties properties = new Properties();
        properties.load(new FileInputStream("cfg/aggregatorMongo.cfg"));
        this.propertiesMap = new HashMap<>();
        Set<String> data = properties.stringPropertyNames();
        for(String key : data){
            propertiesMap.put(key, Arrays.asList(properties.getProperty(key).split(",")));
        }

        this.user = propertiesMap.get("mongoUser2").get(0);
        this.pass = propertiesMap.get("mongoPass2").get(0).toCharArray();
        this.dbName = propertiesMap.get("mongoDBName2").get(0);
        /*this.user = properties.getProperty("mongoUser");
        this.pass = properties.getProperty("mongoPass").toCharArray();
        this.dbName = properties.getProperty("mongoDBName");*/
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
