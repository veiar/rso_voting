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
        Statistics stats = null;

        try {
            mongoDB = new MongoHandler();
            postDB = new PostgresHandler();
            stats = new Statistics();
            //postDB.getDictData(D_CANDIDATES_TABLENAME, D_CANDIDATES_COLS);
            stats.calcAgeFromPesel("901204");
            //mongoDB.test(stats);
            mongoDB.getData();
            //postDB.insert(1, "trololo");
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
