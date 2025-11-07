import java.io.*;
import java.net.*;
import java.util.*;

class Receiver extends Thread {
    private Socket socket;

    Receiver(Socket socket) {
        this.socket = socket;
        start();
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            OutputStream os = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            String id = "MGCompanies";
            bw.write(id);
            bw.newLine();
            bw.flush();

            String request;
            while ((request = br.readLine()) != null) { // wait until \n
                System.out.println("Client sent: " + request);
                if (request.equalsIgnoreCase("END")) break;
            }

            socket.close();
        } catch (Exception e) {
            System.out.println("Receiver error: " + e);
        }
    }
}

class Sender extends Thread {
    private Socket socket;
    private Queue<String> queue;

    Sender(Socket socket) {
        this.socket = socket;
        this.queue = new ArrayDeque<>();
        start();
    }

    public void addToQueue(String msg) {
        this.queue.add(msg);
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            OutputStream os = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            String request = br.readLine(); // read password
            if (request == null || !request.equals("MGCompanies")) {
                bw.write("Invalid Credentials");
                bw.newLine();
                bw.flush();
                socket.close();
                return;
            }

            System.out.println("Client authenticated successfully!");

            while (true) {
                if (!queue.isEmpty()) {
                    while (!queue.isEmpty()) {
                        String msg = queue.poll();
                        bw.write(msg);
                        bw.newLine(); // mark message end
                    }
                    bw.flush();
                }
                Thread.sleep(200); // avoid CPU spin
            }
        } catch (Exception e) {
            System.out.println("Sender error: " + e);
        }
    }
}

class Server {
    private ServerSocket serverSocket1;
    private ServerSocket serverSocket2;
    private Receiver receiver;
    private Sender sender;

    Server() {
        try {
            serverSocket1 = new ServerSocket(5050);
            serverSocket2 = new ServerSocket(6000);
            startListening();
        } catch (Exception e) {
            System.out.println("Server init error: " + e);
        }
    }

    public void startMessageTimer() {
        Timer t = new Timer();
        final int[] count = {1};
        t.scheduleAtFixedRate(new TimerTask() {
            int runs = 0;

            public void run() {
                if (runs >= 3) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 3; i++) {
                    sender.addToQueue("Hi #" + count[0]);
                }
                count[0]++;
                runs++;
            }
        }, 10000, 5000); // start after 10 sec, repeat every 5 sec
    }

    public void startListening() {
        try {
            while (true) {
                System.out.println("Server listening on port 5050...");
                Socket socket1 = serverSocket1.accept();
                receiver = new Receiver(socket1);

                System.out.println("Server listening on port 6000...");
                Socket socket2 = serverSocket2.accept();
                sender = new Sender(socket2);

                startMessageTimer();
            }
        } catch (Exception e) {
            System.out.println("Server listening error: " + e);
        }
    }

    public static void main(String args[]) {
        new Server();
    }
}
