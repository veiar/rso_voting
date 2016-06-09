package RsoAggregator;

import DBHandler.ConnectionHandler;
import DBHandler.MongoHandler;
import DBHandler.PostgresHandler;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.RunnableFuture;
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

    public class RsoAggregator implements Observer {
        public static Logger logger = Logger.getLogger("RSO-Aggregator");
        private boolean master;
        private MongoHandler mongoDB = null;
        private PostgresHandler postDB = null;
        private Statistics stats = null;
        private ConnectionHandler connectionHandler = null;

        public RsoAggregator(boolean _master) {
            this.master = _master;
            stats = new Statistics();
            //final RsoAggregator ag = this;
            /*Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    ConnectionHandler connectionHandler= new ConnectionHandler(master, ag);
                }
            });
            t.start();
*/
            if(master){
                while(true) {
                    init();
                    try {
                        Thread.sleep(10000);
                    }catch (Exception e){

                    }
                }
            }
            else{
                    while (true) {
                        try {
                        Process p1 = Runtime.getRuntime().exec("ping -c 1 52.40.243.126");
                        int ret = p1.waitFor();
                        System.out.println("PING: " + ret);
                        if (ret != 0) {
                            init();
                        }
                        else{
                            Thread.sleep(10000);
                        }
                        } catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
            }
        }

        public boolean getMaster(){
            return this.master;
        }

        private void prepareDB(){
            try {
                mongoDB = new MongoHandler(stats);
                postDB = new PostgresHandler(stats);
                process();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (mongoDB != null) {
                        mongoDB.close();
                        logger.log(Level.INFO, "MongoDB connection closed...");
                        System.out.println("MongoDB connection closed...");
                    }
                    if (postDB != null) {
                        postDB.close();
                        logger.log(Level.INFO, "Postgres connection closed...");
                        System.out.println("Postgres connection closed...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        private void createLog(){
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
            } catch (Exception e) {
                System.err.println("Creating log file failed! Bye!");
                System.exit(4);
            }
        }
        private void prepareConnection(){

        }

        private void init() {
            createLog();
            prepareDB();
            //prepareConnection();
        }

        public void process() {
            try {
                postDB.getDictionaries();
                mongoDB.getAllData();
                postDB.insertStats();

                /*int count = 0;
                while(count < 100) {
                    mongoDB.getAllData();
                    postDB.insertStats();
                    mongoDB.clear();
                    System.gc();
                    Thread.sleep(10000);
                    count++;
                }*/

                //mongoDB.getResults();
                //mongoDB.insertSome();
                //mongoDB.getData();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (mongoDB != null) {
                        mongoDB.close();
                        logger.log(Level.INFO, "MongoDB connection closed...");
                        System.out.println("MongoDB connection closed...");
                    }
                    if (postDB != null) {
                        postDB.close();
                        logger.log(Level.INFO, "Postgres connection closed...");
                        System.out.println("Postgres connection closed...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void update(Observable observable, Object o) {
            // master down

            // master up
        }
    }

