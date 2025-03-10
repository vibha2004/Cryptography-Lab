import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MD5Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("MD5 Server started. Listening on port 12345...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     Scanner in = new Scanner(clientSocket.getInputStream());
                     Scanner scanner = new Scanner(System.in)) {

                    // Read input from the client
                    String input = in.nextLine();
                    if (input != null) {
                        // Compute MD5 hash (assuming MD5Manual.computeMD5 is implemented)
                        String md5Hash = MD5Manual.computeMD5(input);

                        // Send MD5 hash back to the client
                        clientSocket.getOutputStream().write((md5Hash + "\n").getBytes());

                        System.out.println("Calculated MD5 for: " + input + ", sent to client.");
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}