import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5050);
        System.out.println("Connected to server!");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);

        // Receive password request
        String serverPass = in.readLine();
        System.out.println("We got the password: " + serverPass);
        out.println(serverPass); // send it back to authenticate

        // Start two-way chat
        Thread sender = new Thread(() -> {
            try {
                String msg;
                while (true) {
                    System.out.print("Enter a message: ");
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
                        System.out.println("Server ended the chat.");
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) { }
        });

        sender.start();
        receiver.start();

        // Wait for both threads to finish
        sender.join();
        receiver.join();

        System.out.println("Closing connection...");
        socket.close();
    }
}
