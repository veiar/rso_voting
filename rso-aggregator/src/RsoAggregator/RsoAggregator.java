package RsoAggregator;


import MongoHandler.MongoHandler;

public class RsoAggregator {
    public static void main(String[] args){
        MongoHandler mongoDB = null;

        try {
            mongoDB = new MongoHandler();
            mongoDB.test();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (mongoDB != null) {
                    mongoDB.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
