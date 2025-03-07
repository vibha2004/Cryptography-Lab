
// Client Side (MD5Client.java)
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MD5Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345); // Replace "localhost" with server IP if needed
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter a string to get its MD5 hash: ");
            String input = scanner.nextLine();

            out.println(input);
            String md5Hash = in.readLine();

            if (md5Hash != null) {
                System.out.println("MD5 Hash: " + md5Hash);
            } else {
                System.out.println("Server returned null.");
            }

        } catch (UnknownHostException e) {
            System.err.println("Host not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}