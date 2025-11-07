import java.io.*;
import java.net.*;
import java.util.*;

class CReceiver extends Thread {
    private Socket socket;

    CReceiver(Socket socket) {
        this.socket = socket;
        start();
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String response;
            while ((response = br.readLine()) != null) {
                System.out.println("Server: " + response);
                if (response.equalsIgnoreCase("Sayonara")) break;
            }

            socket.close();
        } catch (Exception e) {
            System.out.println("CReceiver error: " + e);
        }
    }
}

class CSender extends Thread {
    private Socket socket;

    CSender(Socket socket) {
        this.socket = socket;
        start();
    }

    public void run() {
        try {
            OutputStream os = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Enter a message: ");
                String input = sc.nextLine();
                bw.write(input);
                bw.newLine(); // delimiter
                bw.flush();

                if (input.equalsIgnoreCase("END")) break;
            }

            socket.close();
        } catch (Exception e) {
            System.out.println("CSender error: " + e);
        }
    }
}

class Client {
    private static Socket socket1;
    private static Socket socket2;
    private static CSender sender;
    private static CReceiver receiver;

    public static void main(String args[]) {
        try {
            // Connect to first port to receive password/id
            socket1 = new Socket("localhost", 5050);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket1.getInputStream()));

            String password = br.readLine(); // read ID sent by server
            System.out.println("We got the password: " + password);

            sender = new CSender(socket1); // start sending messages to port 5050

            // Connect to second port to receive messages from server
            socket2 = new Socket("localhost", 6000);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));
            bw.write(password);
            bw.newLine();
            bw.flush();

            receiver = new CReceiver(socket2);

        } catch (Exception e) {
            System.out.println("Client error: " + e);
        }
    }
}
