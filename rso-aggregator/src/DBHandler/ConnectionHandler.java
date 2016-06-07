package DBHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class ConnectionHandler {

    private Socket socketServer, socketClient, socket;
    ServerSocket serverSocket;
    private boolean master;


    public ConnectionHandler(boolean _master) {
        this.master = _master;
        if(master) {
            ServerRSO server = new ServerRSO();
            server.run();

        }
        else{
            ClientRSO client = new ClientRSO();
            client.run();
        }
    }

    private class ServerRSO extends Thread{
        ServerSocket providerSocket;
        Socket connection = null;
        ObjectOutputStream out;
        ObjectInputStream in;
        String message;
        ServerRSO(){}

        @Override
        public void run()
        {
            try{
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
                sendMessage("Connection successful");
                //4. The two parts communicate via the input and output streams
                do{
                    try{
                        message = (String)in.readObject();
                        System.out.println("client>" + message);
                        if (message.equals("bye"))
                            sendMessage("bye");
                    }
                    catch(ClassNotFoundException classnot){
                        System.err.println("Data received in unknown format");
                    }
                }while(!message.equals("bye"));
                System.exit(0);
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
            finally{
                try{
                    in.close();
                    out.close();
                    providerSocket.close();
                }
                catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }
        }
        void sendMessage(String msg)
        {
            try{
                out.writeObject(msg);
                out.flush();
                System.out.println("server>" + msg);
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }

    }

    public class ClientRSO extends Thread{
        Socket requestSocket;
        ObjectOutputStream out;
        ObjectInputStream in;
        String message;
        ClientRSO(){}

        @Override
        public void run()
        {
            try{
                //1. creating a socket to connect to the server
                requestSocket = new Socket("localhost", 8080);
                System.out.println("Connected to localhost in port 2004");
                //2. get Input and Output streams
                out = new ObjectOutputStream(requestSocket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(requestSocket.getInputStream());
                //3: Communicating with the server
                do{
                    try{
                        message = (String)in.readObject();
                        System.out.println("server>" + message);
                        sendMessage("Hi my server");
                        message = "bye";
                        sendMessage(message);
                    }
                    catch(ClassNotFoundException classNot){
                        System.err.println("data received in unknown format");
                    }
                }while(!message.equals("bye"));
                System.exit(0);
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
                    in.close();
                    out.close();
                    requestSocket.close();
                }
                catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }

        }
        void sendMessage(String msg)
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
