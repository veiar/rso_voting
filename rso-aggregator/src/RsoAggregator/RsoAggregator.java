package RsoAggregator;


import DBHandler.MongoHandler;
import DBHandler.PostgresHandler;

public class RsoAggregator {
    public static void main(String[] args){
        MongoHandler mongoDB = null;
        PostgresHandler post = null;

        try {
            mongoDB = new MongoHandler();
            mongoDB.test();
            post = new PostgresHandler();

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
                if (post != null){
                    post.close();
                    System.out.println("Postgres connection closed...");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
