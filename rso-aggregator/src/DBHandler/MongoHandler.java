package DBHandler;

import RsoAggregator.Statistics;

import com.mongodb.*;
import com.mongodb.client.*;
import com.sun.corba.se.spi.monitoring.MonitoringFactories;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static RsoAggregator.RsoAggregator.logger;

//db.createUser({"user": "pm", "pwd": "pass123", roles: ["readWrite", "dbAdmin"]})
public class MongoHandler {
    private static String m_userName = "pm";
    private static String m_password = "pass123";
    private static String m_dbName = "dbNew";
    private static String m_port = "27017";
    private static String m_dbCollection = "testColl";
    // TODO wypieprzyc
    private String user, user2;// = "pm";
    private char[] pass, pass2;// = "pass123".toCharArray();
    private String dbName, dbName2;// = "testDB";
    private String dbAddress, dbAddress2;
    private String dbCollection, dbCollection2;
    private MongoClient mongoClient, mongoClient2;
    private MongoDatabase mongoDB, mongoDB2;
    // TODO end
    private Statistics stats;
    private Map<String, VoteInfo> mapAllData;
    private List<MongoInstance> mapMongoInstances;
    public MongoHandler(Statistics s){
        stats = s;
        mapAllData = new HashMap<>();
        mapMongoInstances = new ArrayList<>();

        init();
    }
    public void clearStats(){
        stats.clear();
    }
    public class VoteInfo{
        VoteInfo(String _pesel, int _vote, int _votingArea, int _gender, int _education, String _rowId){
            this.pesel = _pesel;
            this.vote = _vote;
            this.votingArea = _votingArea;
            this.gender = _gender;
            this.education = _education;
            this.rowId = _rowId;
        }
        private String pesel;
        private int vote;
        private int votingArea;
        private int gender;
        private int education;
        private String rowId;

        public String getPesel() { return pesel; }
        public int getVote() { return vote; }
        public int getVotingArea() { return votingArea; }
        public int getGender() { return gender; }
        public int getEducation() { return education; }
    }

    public void getAllData(){
        List<Map<String, VoteInfo>> aggregList = new ArrayList<>();
        for(int i=0; i<mapMongoInstances.size(); ++i){
            System.out.println("Getting data from " + i + ". Mongo instance... " + new GregorianCalendar().getTime());
            MongoInstance mi = mapMongoInstances.get(i);
            MongoCollection coll = mi.getMongoDB().getCollection(mi.getDbCollection());
            final Map<String, VoteInfo> map = new HashMap<>();
            BasicDBObject noId = new BasicDBObject();
            noId.append("_id", 0);
            FindIterable output = coll.find();
            output.forEach(new Block<Document>(){
                @Override
                public void apply(final Document document) {
                    String rowId = document.getString("rowId");
                    VoteInfo vi = new VoteInfo(
                            document.getString("PESEL"),
                            document.getInteger("Vote"),
                            document.getInteger("VotingArea"),
                            document.getInteger("Gender"),
                            document.getInteger("Education"),
                            rowId);
                    map.put(rowId, vi);
                    //System.out.println(document);
                }
            });
            aggregList.add(map);
        }
        removeDuplicates(aggregList);
        stats.calculateAll(this.mapAllData);
    }

    public void clear(){
        this.mapAllData.clear();
        clearStats();
    }

    private void removeDuplicates(List<Map<String, VoteInfo>> list){
        System.out.println("Removing duplicates... " + new GregorianCalendar().getTime());
        for(int i=0; i<list.size(); ++i){
            this.mapAllData.putAll(list.get(i));
        }   // 8k wierszy z drugiej bazy ma to samo rowId i wylecialo czyli niby dziala
    }

    public Statistics getStats() {return this.stats;}
    private void init(){
        getProperties();

        while(!setConnectionToAllMongo()){
            logger.log(Level.SEVERE, "Unable to connect to any Mongo instance, retrying in 5 sec...");
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e){
                logger.log(Level.SEVERE, "Internal error!");
            }
        }

/*        MongoCredential cred = MongoCredential.createCredential(user, dbName, pass);
        MongoCredential cred2 = MongoCredential.createCredential(user2, dbName2, pass2);
        //this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        this.mongoClient = new MongoClient(new ServerAddress(this.dbAddress), Arrays.asList(cred));
        this.mongoDB = mongoClient.getDatabase(dbName);

        this.mongoClient2 = new MongoClient(new ServerAddress(this.dbAddress2), Arrays.asList(cred2));
        this.mongoDB2 = mongoClient2.getDatabase(dbName2);
*/

    }

    private class MongoInstance{
        private String user;
        private char[] pass;
        private String dbName;
        private String dbAddress;
        private String dbCollection;
        private MongoClient mongoClient;
        private MongoDatabase mongoDB;
        public String getUser() { return user; }
        public char[] getPass() {return pass; }
        public String getDbName() { return dbName; }
        public String getDbAddress() { return dbAddress; }
        public String getDbCollection() { return dbCollection; }
        public MongoClient getMongoClient() { return mongoClient; }
        public MongoDatabase getMongoDB() { return mongoDB; }

        MongoInstance(String _user, char[] _pass, String _dbName, String _dbAddress, String _dbCollection){
            this.user = _user;
            this.pass = _pass;
            this.dbName = _dbName;
            this.dbAddress = _dbAddress;
            this.dbCollection = _dbCollection;
            this.mongoClient = null;
            this.mongoDB = null;
        }

        public boolean setConnection(){
            try {
                MongoCredential cred = MongoCredential.createCredential(user, dbName, pass);

                this.mongoClient = new MongoClient(new ServerAddress(this.dbAddress), Arrays.asList(cred),
                        MongoClientOptions.builder().serverSelectionTimeout(1000).build());     // timeout = 1s
                this.mongoDB = mongoClient.getDatabase(dbName);
                Document doc = mongoDB.runCommand(new BasicDBObject("ping", 1));
                logger.log(Level.INFO, "Connected to MongoDB! -host: " + dbAddress + " -user: " + user
                        + " -dbName: " + dbName + " -collection: " + dbCollection);
                return true;
            }catch (Exception e){
                logger.log(Level.SEVERE, "Failed to connect to MongoDB! Timeout reached! -host: " + dbAddress + " -user: " + user
                        + " -dbName: " + dbName + " -collection: " + dbCollection);
                return false;
            }
        }

        public boolean checkStatus(){
            try{
                Document doc = mongoDB.runCommand(new BasicDBObject("ping", 1));
            } catch (Exception e){
                logger.log(Level.SEVERE, "Failed to ping MongoDB! " + dbAddress);
                return false;
            }
            return true;
        }
    }

    // zwraca true jesli udalo sie polaczyc do jakiejkolwiek bazy mongo
    private boolean setConnectionToAllMongo(){
        int mongoCount = 0;
        for(MongoInstance mi : mapMongoInstances){
            if(mi.setConnection()) {
                mongoCount++;
            }
        }
        return mongoCount > 0;
    }

    private void getProperties(){
        Map<String, String> env = System.getenv();
        if(!env.containsKey("MONGO_HOSTS") || !env.containsKey("POSTGRES_HOSTS")){
            System.err.println("No environment variables set! Bye!");
            logger.log(Level.SEVERE, "No environment variables set! Bye!");
            System.exit(2);
        }
        String mongoAllHosts = env.get("MONGO_HOSTS");
        String postgresAllHosts = env.get("POSTGRES_HOSTS");
        String[] mongoHosts = mongoAllHosts.split(",");
        String[] postgresHosts = postgresAllHosts.split(",");

        for(String t : mongoHosts){
            MongoInstance mi = new MongoInstance(
                    m_userName,
                    m_password.toCharArray(),
                    m_dbName,
                    t + ":" + m_port,
                    m_dbCollection
            );
            //mi.setConnection();
            mapMongoInstances.add(mi);  // moze uda sie pozniej polaczyc, wiec i tak dodajemy do mapy
        }
/*
        java.util.Properties properties = new Properties();
        properties.load(new FileInputStream("cfg/aggregatorMongo.cfg"));
        Map<String, List<String>> propertiesMap = new HashMap<>();
        Set<String> data = properties.stringPropertyNames();
        for(String key : data){
            propertiesMap.put(key, Arrays.asList(properties.getProperty(key).split(",")));
        }

        for(int i=0; i < propertiesMap.get("mongoUser2").size(); ++i){
            MongoInstance mi = new MongoInstance(
                    propertiesMap.get("mongoUser2").get(i),
                    propertiesMap.get("mongoPass2").get(i).toCharArray(),
                    propertiesMap.get("mongoDBName2").get(i),
                    propertiesMap.get("mongoDBAddress2").get(i),
                    propertiesMap.get("mongoDBCollection2").get(i)
            );
        mi.setConnection();
        mapMongoInstances.add(mi);

        }
        this.user = propertiesMap.get("mongoUser2").get(0);
        this.pass = propertiesMap.get("mongoPass2").get(0).toCharArray();
        this.dbName = propertiesMap.get("mongoDBName2").get(0);
        this.dbAddress = propertiesMap.get("mongoDBAddress2").get(0);
        this.dbCollection = propertiesMap.get("mongoDBCollection2").get(0);

        this.user2 = propertiesMap.get("mongoUser2").get(1);
        this.pass2 = propertiesMap.get("mongoPass2").get(1).toCharArray();
        this.dbName2 = propertiesMap.get("mongoDBName2").get(1);
        this.dbAddress2 = propertiesMap.get("mongoDBAddress2").get(1);
        this.dbCollection2 = propertiesMap.get("mongoDBCollection2").get(1);*/
        /*this.user = properties.getProperty("mongoUser");
        this.pass = properties.getProperty("mongoPass").toCharArray();
        this.dbName = properties.getProperty("mongoDBName");*/
    }
    public void close(){
       /* this.mongoClient.close();
        this.mongoClient2.close();*/

        for(int i=0; i < mapMongoInstances.size(); ++i){
            mapMongoInstances.get(0).mongoClient.close();
        }
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

    public void getResults(){
        System.out.println("Getting and calculating results from Mongo started on " + new GregorianCalendar().getTime());
        getResPartyCandidates();
        getResPartyEducation();
        getResPartySex();
        getResPartyConstituency();
        getResPartyAge();
        System.out.println("Getting and calculating results from Mongo ended on " + new GregorianCalendar().getTime());
    }

    public void getResPartyAge(){
        MongoCollection collM = mongoDB.getCollection(dbCollection);

        BasicDBObject fields = new BasicDBObject("Vote", 1);
        //fields.put("Birth", new BasicDBObject("$substr", "\"$PESEL\", 0, 2"));
        fields.put("Birth", new BasicDBObject("$substr", new Object[] {"$PESEL", 0, 6}));
        fields.put("_id", 0);
        BasicDBObject project = new BasicDBObject("$project", fields );

        AggregateIterable output =  collM.aggregate(Arrays.asList(project));
        stats.calcResPartyAge(output);
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


    private void getResPartyCandidates(){
        MongoCollection coll = mongoDB.getCollection(dbCollection);
        BasicDBObject groupFields = new BasicDBObject( "_id", "$Vote");
        groupFields.put("sum", new BasicDBObject( "$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        AggregateIterable output =  coll.aggregate(Arrays.asList(group));
        stats.calcResPartyCandidates(output);
    }

    private void getResPartyEducation(){
        MongoCollection coll = mongoDB.getCollection(dbCollection);

        BasicDBObject x = new BasicDBObject();
        x.append("Vote", "$Vote")
                .append("Education", "$Education");
        BasicDBObject groupFields = new BasicDBObject( "_id", x);
        groupFields.put("sum", new BasicDBObject( "$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        AggregateIterable output =  coll.aggregate(Arrays.asList(group));
        stats.calcResPartyEducation(output);
    }

    private void getResPartySex(){
        MongoCollection coll = mongoDB.getCollection(dbCollection);

        BasicDBObject x = new BasicDBObject();
        x.append("Vote", "$Vote")
                .append("Gender", "$Gender");
        BasicDBObject groupFields = new BasicDBObject( "_id", x);
        groupFields.put("sum", new BasicDBObject( "$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        AggregateIterable output =  coll.aggregate(Arrays.asList(group));
        stats.calcResPartySex(output);
    }

    private void getResPartyConstituency(){
        MongoCollection coll = mongoDB.getCollection(dbCollection);

        BasicDBObject x = new BasicDBObject();
        x.append("Vote", "$Vote")
                .append("VotingArea", "$VotingArea");
        BasicDBObject groupFields = new BasicDBObject( "_id", x);
        groupFields.put("sum", new BasicDBObject( "$sum", 1));
        BasicDBObject group = new BasicDBObject("$group", groupFields);

        AggregateIterable output =  coll.aggregate(Arrays.asList(group));
        stats.calcResPartyConstituency(output);
    }



    public void insertSome(){
        Random random = new Random();
        boolean gender = false, edu = true;
        ArrayList<Document> docList = new ArrayList<>();
        for(int i = 0; i < 5000; ++i) {
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
                    .append("Gender", random.nextInt(2))
                    .append("Address", random.nextInt(100))
                    .append("Education", random.nextInt(5))
                    .append("rowId", UUID.randomUUID().toString());
            //mongoDB.getCollection("testColl").insertOne(obj);

            gender = !gender;
            edu = !edu;

            docList.add(obj);
        }
        //mongoDB.getCollection(dbCollection).insertMany(docList);
        mongoDB2.getCollection(dbCollection2).insertMany(docList);
    }

}
