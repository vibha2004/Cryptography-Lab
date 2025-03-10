import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AliceClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             Scanner in = new Scanner(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");
            socket.getOutputStream().write(("Alice\n").getBytes()); // Identify as Alice

            // Receive q and alpha from the server
            int q = in.nextInt();
            int alpha = in.nextInt();
            System.out.println("Received q = " + q + " and alpha = " + alpha + " from server.");

            // Take Alice's private key from user
            System.out.print("Enter Alice's private key (Xa): ");
            int Xa = scanner.nextInt();
            System.out.println("Alice's private key (Xa): " + Xa);

            // Compute Alice's public key
            int Ya = (int) (Math.pow(alpha, Xa) % q);
            System.out.println("Alice's public key (Ya): " + Ya);

            // Send Ya to the server
            socket.getOutputStream().write((Ya + "\n").getBytes());

            // Receive Yd2 from the server
            int Yd2 = in.nextInt();
            System.out.println("Received Yd2 from Eve: " + Yd2);

            // Compute shared key
            int K = (int) (Math.pow(Yd2, Xa) % q);
            System.out.println("Alice computed shared key (K): " + K);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}