package RsoAggregator;

import DBHandler.MongoHandler;
import DBHandler.PostgresHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

 class LoggerWrapper {
    public static final Logger logger = Logger.getLogger("RSO-Aggregator-Logger");
    private static LoggerWrapper instance = null;

    public static LoggerWrapper getInstance() {
        if (instance == null) {
            prepareLogger();
            instance = new LoggerWrapper();
        }
        return instance;
    }

    private static void prepareLogger() {
        try {

            FileHandler myFileHandler = new FileHandler(new Date().getTime() + ".log", true);
            myFileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(myFileHandler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            System.err.println("Error while creating log file! Bye!");
            System.exit(3);
        }
    }
}

    public class RsoAggregator {
        public static Logger logger = Logger.getLogger("RSO-Aggregator");

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
                String pattern = "yyyyMMddHHmmss";
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                Date date = new Date();
                String dateFormatted = format.format(date);
                if (!new File("log").exists()) {
                    boolean success = new File("log").mkdir();
                }

                FileHandler fh = new FileHandler("log/" + dateFormatted + ".log");
                fh.setFormatter(new SimpleFormatter());
                logger.addHandler(fh);
                logger.log(Level.INFO, "--- RSO AGGREGATOR ---\nLog started");
            } catch (Exception e){
                System.err.println("Creating log file failed! Bye!");
                System.exit(4);
            }
            try{
                mongoDB = new MongoHandler(stats);
                postDB = new PostgresHandler(stats);
                postDB.getDictionaries();
                mongoDB.getAllData();
                postDB.insertStats();
                //ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                //executor.scheduleWithFixedDelay(new WorkerThread(), 10, 5, TimeUnit.SECONDS)
                //executor.scheduleWithFixedDelay(new Task(mongoDB, postDB), 1, 15, TimeUnit.SECONDS);
                /*int count = 0;
                while(count < 10) {
                    mongoDB.clear();
                    mongoDB.getAllData();
                    postDB.insertStats();
                    Thread.sleep(10000);
                    count++;
                }*/

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
