import DBHandler.ConnectionHandler;
import RsoAggregator.RsoAggregator;

public class Main {

    public static void main(String[] args) {
        boolean master = false;
        try {
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
        }

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
