package DBHandler;

import RsoAggregator.Statistics;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;
import org.jongo.Aggregate;
import org.jongo.Find;
import org.jongo.Jongo;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jongo.marshall.jackson.oid.MongoId;

//db.createUser({"user": "pm", "pwd": "pass123", roles: ["readWrite", "dbAdmin"]})
public class MongoHandler {
    private String user;// = "pm";
    private char[] pass;// = "pass123".toCharArray();
    private String dbName;// = "testDB";
    private String dbAddress;
    private String dbCollection;
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    private Map<String, List<String>> propertiesMap;
    Statistics stats = null;
    public MongoHandler(){
        init();
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
        this.mongoClient = new MongoClient(new ServerAddress(this.dbAddress), Arrays.asList(cred));
        this.mongoDB = mongoClient.getDatabase(dbName);

        stats = new Statistics();
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
        this.dbAddress = propertiesMap.get("mongoDBAddress2").get(0);
        this.dbCollection = propertiesMap.get("mongoDBCollection2").get(0);
        /*this.user = properties.getProperty("mongoUser");
        this.pass = properties.getProperty("mongoPass").toCharArray();
        this.dbName = properties.getProperty("mongoDBName");*/
    }
    public void close(){
        this.mongoClient.close();
    }

    public void test(Statistics stats){
        MongoCollection coll = mongoDB.getCollection(dbCollection);
        BasicDBObject c = new BasicDBObject();
        c.append("Gender", "K");
        System.out.println("Women: "+coll.count(c));

/*
        BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("Gender", "K") );
        BasicDBObject fields = new BasicDBObject("Education", 1);
        //fields.put("Education", 1);
        fields.put("_id", 0);
        BasicDBObject project = new BasicDBObject("$project", fields );

        BasicDBObject groupFields = new BasicDBObject( "_id", "$Education");
        groupFields.put("count", new BasicDBObject( "$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        AggregateIterable output =  coll.aggregate(Arrays.asList(match, project, group));

        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                //Object temp = document.get("Gender");
                System.out.println(document);
            }
        });*/

        BasicDBObject d = new BasicDBObject();
        d.append("Gender", "K");

        //d.append("$substr", new Document("$PESEL", new int[] {0, 6}));
        coll.count(d);
        FindIterable<Document> iterable = mongoDB.getCollection(dbCollection).find(d);

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                //Object temp = document.get("Gender");
                System.out.println(document);
            }
        });

        //stats.setWomenAllCount(mongoDB.getCollection(dbCollection).count(d));

        //insertSome();

    }

    private class PeselData{
        @MongoId
        private String key;
        @JsonProperty("PESEL")
        private int pesel;
        @JsonProperty("Vote")
        private int vote;
        @JsonProperty("Birth")
        private String birth;
        PeselData() {}

        PeselData(int a, int b, String c){
            this.pesel = a;
            this.vote = b;
            this.birth = c;
        }

        public void setPesel(int a) { this.pesel = a; }
        public void setVote(int v) { this.vote = v; }
        public void setKey(String k) { this.key = k; }
        public void setBirth(String b) {this.birth = b; }
    }

    public void getData(){
        MongoCollection collM = mongoDB.getCollection(dbCollection);
        DB db = mongoClient.getDB(dbName);
        Jongo jongo = new Jongo(db);
        org.jongo.MongoCollection coll = jongo.getCollection(dbCollection);
        /*BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("Gender", "K") );
        BasicDBObject fields = new BasicDBObject("PESEL", new BasicDBObject());
        fields.put("$substr", new int[] {0, 6});
        fields.put("_id", 0);
        BasicDBObject project = new BasicDBObject("$project", fields );
*/
        //Aggregate.ResultsIterator a = coll.aggregate("{$project: {PESEL:1, Vote:1, Birth: { $substr: [\"$PESEL\", 0, 6]}, _id:0 } }").as(Object.class);
        //Aggregate.ResultsIterator a = coll.aggregate("{$project: {PESEL:1, _id:0 } }").as(String.class);
        //AggregateIterable ai = collM.aggregate();

       //org.jongo.MongoCursor<String> f = coll.find().projection("{ppp: { $substr: [\"$PESEL\", 0, 6] } }").as(String.class);
        /*org.jongo.MongoCursor<Object> f = coll.find().projection("{_id : 0, " +
                "PESEL: 1, " +
                //"trunc : { $substr: [\"$PESEL\", 0, 6] }" +
                "}").as(Object.class);
        while (f.hasNext()){
            System.out.println(f.next());
        }*/

        BasicDBObject fields = new BasicDBObject("PESEL", 1);
        //fields.put("Birth", new BasicDBObject("$substr", "\"$PESEL\", 0, 2"));
        fields.put("Birth", new BasicDBObject("$substr", new Object[] {"$PESEL", 0, 6}));
        fields.put("_id", 0);
        BasicDBObject project = new BasicDBObject("$project", fields );

        AggregateIterable output =  collM.aggregate(Arrays.asList(project));
        output.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                int temp = Integer.parseInt((String)document.get("Birth"));
                System.out.println(temp);
            }
        });
        /*while (a.hasNext()){
            System.out.println(a.next());
        }*/


        //AggregateIterable output =  coll.aggregate(Arrays.asList(match, project));
/*
        f.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document) {
                //Object temp = document.get("Gender");
                System.out.println(document);
            }
        });*/
    }


    public void getResPartyCandidates(){
        MongoCollection coll = mongoDB.getCollection(dbCollection);
        BasicDBObject groupFields = new BasicDBObject( "_id", "$Vote");
        groupFields.put("sum", new BasicDBObject( "$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        AggregateIterable output =  coll.aggregate(Arrays.asList(group));
        stats.calcResPartyCandidates(output);

    }

    public void insertSome(){
        Random random = new Random();
        boolean gender = false, edu = true;
        ArrayList<Document> docList = new ArrayList<>();
        for(int i = 0; i < 1000; ++i) {
            int year = random.nextInt(99)+1;
            int month = random.nextInt(12)+1;
            int day = random.nextInt(31)+1;
            int rest = random.nextInt(89999)+10000;
            String y = Integer.toString(year);
            if(y.length() == 1) y = "0" + y;
            String m = Integer.toString(month);
            if(m.length() == 1) m = "0" + m;
            String d = Integer.toString(day);
            if(d.length() == 1) d = "0" + d;

            String pesel = y+m+d+rest;

            Document obj = new Document("PESEL", pesel)
                    .append("Vote", random.nextInt(15))
                    .append("VotingArea", random.nextInt(16))
                    .append("Gender", gender ? "M" : "K")
                    .append("Address", random.nextInt(100))
                    .append("Education", edu ? "High" : "Low");
            //mongoDB.getCollection("testColl").insertOne(obj);

            gender = !gender;
            edu = !edu;

            docList.add(obj);
        }
        mongoDB.getCollection(dbCollection).insertMany(docList);
    }


    private void calcStats(){

    }
}
