import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MD5Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345); // Replace "localhost" with server IP if needed
             Scanner in = new Scanner(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter a string to get its MD5 hash: ");
            String input = scanner.nextLine();

            // Send input to the server
            socket.getOutputStream().write((input + "\n").getBytes());

            // Receive MD5 hash from the server
            String md5Hash = in.nextLine();

            if (md5Hash != null) {
                System.out.println("MD5 Hash: " + md5Hash);
            } else {
                System.out.println("Server returned null.");
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}