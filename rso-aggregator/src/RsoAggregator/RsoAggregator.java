package RsoAggregator;


import DBHandler.MongoHandler;
import DBHandler.PostgresHandler;

public class RsoAggregator {
    public static void main(String[] args){
        MongoHandler mongoDB = null;
        PostgresHandler postDB = null;

        try {
            mongoDB = new MongoHandler();
            mongoDB.test();
            postDB = new PostgresHandler();
            postDB.insert(1, "trololo");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (mongoDB != null) {
                    mongoDB.close();
                    System.out.println("MongoDB connection closed...");
                }
                if (postDB != null){
                    postDB.close();
                    System.out.println("Postgres connection closed...");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
