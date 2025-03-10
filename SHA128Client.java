import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SHA128Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             Scanner in = new Scanner(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the SHA-128 server.");

            // Step 1: Input 512-bit message (64 bytes)
            System.out.print("Enter a 512-bit (64-byte) message in hexadecimal: ");
            String input = scanner.nextLine().trim();

            // Validate input length (64 bytes = 128 hex characters)
            if (input.length() != 128) {
                System.out.println("Input must be exactly 128 hexadecimal characters (512 bits).");
                return;
            }

            // Send the input to the server
            socket.getOutputStream().write((input + "\n").getBytes());

            // Receive the hash from the server
            String hash = in.nextLine();
            System.out.println("SHA-128 Hash: " + hash);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}