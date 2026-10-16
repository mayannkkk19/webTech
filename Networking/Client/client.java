import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(s.getInputStream())
        );

        PrintWriter out = new PrintWriter(
            s.getOutputStream(), true
        );

        Scanner sc = new Scanner(System.in);

        Thread t = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("\nReceived: " + msg);
                    System.out.print("You: ");
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });

        t.start();

        System.out.println("Connected to chat!");
        System.out.println("Type exit to quit.");

        while (sc.hasNextLine()) {
            String msg = sc.nextLine();

            if (msg.equalsIgnoreCase("exit")) {
                break;
            }

            out.println(msg);
        }

        s.close();
        sc.close();
    }
}