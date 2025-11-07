import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(5050);
        System.out.println("Server listening on port 5050...");
        Socket socket = ss.accept();
        System.out.println("Client connected!");

        // Streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);

        // Authentication Phase
        out.println("MGCompanies");
        String password = in.readLine();
        if (!"MGCompanies".equals(password)) {
            System.out.println("Authentication failed!");
            socket.close();
            ss.close();
            return;
        }
        System.out.println("Client authenticated successfully!");

        // Start two-way chat
        Thread sender = new Thread(() -> {
            try {
                String msg;
                while (true) {
                    msg = sc.nextLine();
                    out.println(msg);
                    if (msg.equalsIgnoreCase("END")) break;
                }
            } catch (Exception e) { }
        });

        Thread receiver = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("END")) {
                        System.out.println("Client ended the chat.");
                        break;
                    }
                    System.out.println("Client: " + msg);
                }
            } catch (Exception e) { }
        });

        sender.start();
        receiver.start();

        // Wait for both threads
        sender.join();
        receiver.join();

        System.out.println("Closing connection...");
        socket.close();
        ss.close();
    }
}
