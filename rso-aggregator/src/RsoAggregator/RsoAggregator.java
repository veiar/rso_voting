package RsoAggregator;


import DBHandler.MongoHandler;
import DBHandler.PostgresHandler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RsoAggregator {
    public static void main(String[] args){
        MongoHandler mongoDB = null;
        PostgresHandler postDB = null;
        Statistics stats = new Statistics();

        /*class Task implements Runnable{
            private MongoHandler mongoDB;
            private PostgresHandler postDB;
            Task(MongoHandler _mongoDB, PostgresHandler _postDB){
                this.mongoDB = _mongoDB;
                this.postDB = _postDB;
            }
            @Override
            public void run() {
                mongoDB.getAllData();
                postDB.insertStats();
            }
        }*/

        try {
            mongoDB = new MongoHandler(stats);
            postDB = new PostgresHandler(stats);
            postDB.getDictionaries();
            //ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            //executor.scheduleWithFixedDelay(new WorkerThread(), 10, 5, TimeUnit.SECONDS)
            //executor.scheduleWithFixedDelay(new Task(mongoDB, postDB), 1, 15, TimeUnit.SECONDS);
            int count = 0;
            while(count < 10) {
                mongoDB.clear();
                mongoDB.getAllData();
                postDB.insertStats();
                Thread.sleep(10000);
                count++;
            }

            //mongoDB.getResults();
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
