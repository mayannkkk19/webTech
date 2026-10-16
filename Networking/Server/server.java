import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class server {
    static Set<PrintWriter> clients =
        Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server started. Waiting for clients...");

        while (true) {
            Socket s = ss.accept();
            System.out.println("Client connected: " + s.getInetAddress());

            new Thread(() -> {
                PrintWriter out = null;

                try {
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(s.getInputStream())
                    );

                    out = new PrintWriter(s.getOutputStream(), true);
                    clients.add(out);

                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("Client: " + msg);

                        synchronized (clients) {
                            for (PrintWriter p : clients) {
                                p.println(msg);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Client disconnected.");
                } finally {
                    if (out != null) {
                        clients.remove(out);
                    }
                    try {
                        s.close();
                    } catch (IOException e) {
                        // Socket already closed
                    }
                }
            }).start();
        }
    }
}