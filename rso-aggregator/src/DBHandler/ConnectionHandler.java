package DBHandler;

import RsoAggregator.RsoAggregator;

import java.io.*;
import java.net.*;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;


public class ConnectionHandler extends Observable {
    public static int MASTER_DOWN = 11;
    public static int MASTER_UP = 12;
    public static int MSG_OK = 21;
    public static int MSG_IS_ALIVE = 22;
    private Socket socketServer, socketClient, socket;
    ServerSocket serverSocket;
    private boolean master;


    public ConnectionHandler(boolean _master, RsoAggregator _rsoAggregator) {
        this.master = _master;
        addObserver(_rsoAggregator);

        if(master) {
            ServerRSO server = new ServerRSO();
            //server.run();
        }
        else{
            ClientRSO client = new ClientRSO();
            client.run();
        }
    }

    private class ServerRSO {//extends Thread{
        ServerSocket providerSocket;
        Socket connection = null;
        ObjectOutputStream out;
        ObjectInputStream in;
        int message;
        ServerRSO() {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        //1. creating a server socket
                        providerSocket = new ServerSocket(8080);
                        //2. Wait for connection
                        System.out.println("Waiting for connection");
                        connection = providerSocket.accept();
                        System.out.println("Connection received from " + connection.getInetAddress().getHostName());
                        //3. get Input and Output streams
                        out = new ObjectOutputStream(connection.getOutputStream());
                        out.flush();
                        in = new ObjectInputStream(connection.getInputStream());
                        //sendMessage("Connection successful");
                        //4. The two parts communicate via the input and output streams
                        do {
                            try {
                                message = in.read();
                                System.out.println("client>" + message);
                                if (message == MSG_IS_ALIVE)
                                    sendMessage(MSG_OK);
                            } catch (SocketTimeoutException e2) {
                                System.err.println("TIMEOUT while waiting for slave's response!");
                            }
                        } while (true);
                        //System.exit(0);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } finally {
                        try {
                            in.close();
                            out.close();
                            providerSocket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }.run();
        }

                void sendMessage(int msg) {
                    try {
                        out.writeObject(msg);
                        out.flush();
                        System.out.println("server>" + msg);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

        }


    public class ClientRSO extends Thread{
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        int message;
        ClientRSO(){}

        @Override
        public void run()
        {
            try{
                //1. creating a socket to connect to the server
                requestSocket = new Socket();
                requestSocket.setSoTimeout(5000);  // czekam 5s na odp
                requestSocket.connect(new InetSocketAddress("localhost", 8080), 10000);
                System.out.println("Connected to localhost in port 8080");
                //2. get Input and Output streams
                out = new ObjectOutputStream(requestSocket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(requestSocket.getInputStream());
                //3: Communicating with the server
                do{
                    try{
                        sendMessage(MSG_IS_ALIVE);
                        message = in.read();
                        System.out.println("server>" + message);
                        //sendMessage("Hi my server");
                        wait(10000);
                    }
                    catch(InterruptedException e1){
                        System.err.println("ERROR while communicating between master and slave!");
                    }
                    catch(SocketTimeoutException e2){
                        System.err.println("TIMEOUT while waiting for master's response!");
                    }
                }while(true);
                //System.exit(0);
            }
            catch (ConnectException ec){
                System.out.println("Unable to connect to master!");
            }
            catch(UnknownHostException unknownHost){
                System.err.println("You are trying to connect to an unknown host!");
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
            finally{
                //4: Closing connection
                try{
                    if(in != null){
                        in.close();
                    }
                    if(out != null) {
                        out.close();
                    }
                    if(requestSocket != null){
                        requestSocket.close();
                    }
                }
                catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }

        }
        void sendMessage(int msg)
        {
            try{
                out.writeObject(msg);
                out.flush();
                System.out.println("client>" + msg);
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }

    }


    private void masterHandler(){
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter a line with only a period to quit\n");

            // Get messages from the client, line by line; return them
            // capitalized
            while (true) {
                String input = in.readLine();
                if (input == null || input.equals(".")) {
                    break;
                }
                out.println(input.toUpperCase());
            }
        } catch (IOException e) {

        } finally {
            try {
                socket.close();
            } catch (IOException e) {

            }

        }

    }

    private void shadowHandler(){

    }

    public void run() {
        if(master){
            masterHandler();
        }
        else{
            shadowHandler();
        }

    }


}
