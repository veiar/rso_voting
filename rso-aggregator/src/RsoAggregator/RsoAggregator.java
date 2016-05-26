package RsoAggregator;


import DBHandler.MongoHandler;
import DBHandler.PostgresHandler;


public class RsoAggregator {
    public static void main(String[] args){
        MongoHandler mongoDB = null;
        PostgresHandler postDB = null;
        Statistics stats = new Statistics();


        try {
            mongoDB = new MongoHandler(stats);
            postDB = new PostgresHandler(stats);
            postDB.getDictionaries();
            //stats.calcAgeFromPesel("901204");
            mongoDB.getResults();
            postDB.insertStats();
            //mongoDB.insertSome();
            //mongoDB.getData();
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
