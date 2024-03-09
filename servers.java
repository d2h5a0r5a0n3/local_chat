// package Chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class servers {
    private static final int PORT = 1234;
    private static final List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server Connected : Waiting for the Clients");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Connected : "+clientSocket);

                ClientHandler clientHandler=new ClientHandler(clientSocket);    
                clients.add(clientHandler);
                Thread clientThread=new Thread(clientHandler);
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void broadcastMessage(String msg, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (sender != client) {
                client.sendMessage(msg);
            }
        }
    }

    public static synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private Scanner in;

    public ClientHandler(Socket socket) {
        try {
            clientSocket = socket;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            String username=in.nextLine();
            servers.broadcastMessage(username+" has joined the chat.",this);
            String msg;
            while (in.hasNextLine()) {
                msg=in.nextLine();
                servers.broadcastMessage(username+" : "+msg,this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            servers.removeClient(this);
            try{
                clientSocket.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(String str){
        out.println(str);
    }
}
