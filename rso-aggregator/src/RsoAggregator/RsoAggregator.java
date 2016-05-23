package RsoAggregator;


import DBHandler.MongoHandler;
import DBHandler.PostgresHandler;

import java.sql.Array;
import java.sql.ResultSet;

import static DBHandler.PostgresHandler.D_CANDIDATES_COLS;
import static DBHandler.PostgresHandler.D_CANDIDATES_TABLENAME;

public class RsoAggregator {
    public static void main(String[] args){
        MongoHandler mongoDB = null;
        PostgresHandler postDB = null;


        try {
            mongoDB = new MongoHandler();
            postDB = new PostgresHandler();
            //postDB.getDictData(D_CANDIDATES_TABLENAME, D_CANDIDATES_COLS);
            //stats.calcAgeFromPesel("901204");
            mongoDB.getResPartyCandidates();
            postDB.insertResPartyCandidates(mongoDB.getStats());
            //mongoDB.insertSome();
            // mongoDB.getData();
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
