import DBHandler.ConnectionHandler;
import RsoAggregator.RsoAggregator;

import java.util.Map;
import java.util.logging.Level;

import static RsoAggregator.RsoAggregator.logger;

public class Main {

    public static void main(String[] args) {
        boolean master = true;
        Map<String, String> env = System.getenv();
        if(!env.containsKey("MASTER")){
            System.err.println("No master environment variables set! Bye!");
            logger.log(Level.SEVERE, "No environment variables set! Bye!");
            System.exit(2);
        }
        String isMaster = env.get("MASTER");
        if(isMaster.equals("1")){
            master = true;
        }
        else{
            master = false;
        }
        /*try {
            Integer arg = Integer.parseInt(args[0]);
            System.out.println(arg);
            if (arg == 0) {
                master = false;
            } else {
                master = true;
            }
        } catch (Exception e) {
            System.err.println("No parameters! Bye!");

            //System.exit(2);
        }*/

        final RsoAggregator rsoAggregator = new RsoAggregator(master);

        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                new ConnectionHandler(rsoAggregator.getMaster(), rsoAggregator);
            }
        });
        t.start();*/

        //rsoAggregator.process();


    }
}
