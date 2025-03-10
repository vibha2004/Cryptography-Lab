import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BobClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             Scanner in = new Scanner(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");
            socket.getOutputStream().write(("Bob\n").getBytes()); // Identify as Bob

            // Receive q and alpha from the server
            int q = in.nextInt();
            int alpha = in.nextInt();
            System.out.println("Received q = " + q + " and alpha = " + alpha + " from server.");

            // Take Bob's private key from user
            System.out.print("Enter Bob's private key (Xb): ");
            int Xb = scanner.nextInt();
            System.out.println("Bob's private key (Xb): " + Xb);

            // Compute Bob's public key
            int Yb = (int) (Math.pow(alpha, Xb) % q);
            System.out.println("Bob's public key (Yb): " + Yb);

            // Send Yb to the server
            socket.getOutputStream().write((Yb + "\n").getBytes());

            // Receive Yd1 from the server
            int Yd1 = in.nextInt();
            System.out.println("Received Yd1 from Eve: " + Yd1);

            // Compute shared key
            int K = (int) (Math.pow(Yd1, Xb) % q);
            System.out.println("Bob computed shared key (K): " + K);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}