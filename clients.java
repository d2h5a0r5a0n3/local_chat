import java.util.*;
import java.io.*;
import java.net.*;

public class clients {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        final String socket_address = "192.168.1.10"; // Change this to the server's IP address
        final int PORT = 1234;

        try (Socket socket = new Socket(socket_address, PORT)) {
            System.out.println("Connected to the server..");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(socket.getInputStream());

            System.out.print("Enter the username: ");
            String username = sc.nextLine();
            out.println(username);

            Thread thread = new Thread(() -> {
                try {
                    String msg;
                    while (in.hasNextLine()) {
                        msg = in.nextLine();
                        System.out.println(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            // Allow some time for the server to start and accept the connection
            Thread.sleep(1000);

            String msg;
            while (true) {
                msg = sc.nextLine();
                out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
