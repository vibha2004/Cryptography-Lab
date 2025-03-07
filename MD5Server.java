
// Server Side (MD5Server.java)
import java.io.*;
import java.net.*;

public class MD5Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("MD5 Server started. Listening on port 12345...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String input = in.readLine();
                    if (input != null) {
                        String md5Hash = MD5Manual.computeMD5(input);
                        out.println(md5Hash);
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