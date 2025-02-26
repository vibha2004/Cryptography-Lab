import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class RSAEncryptionClient {

    // Square and Multiply method for modular exponentiation
    public static long squareAndMultiply(long base, long exponent, long mod) {
        long result = 1;
        String binaryExponent = Long.toBinaryString(exponent);

        for (int i = 0; i < binaryExponent.length(); i++) {
            result = (result * result) % mod;
            if (binaryExponent.charAt(i) == '1') {
                result = (result * base) % mod;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input values from the user
        System.out.print("Enter the message (text): ");
        String message = scanner.nextLine();
        System.out.print("Enter prime number (p): ");
        long p = scanner.nextLong();
        System.out.print("Enter prime number (q): ");
        long q = scanner.nextLong();
        System.out.print("Enter public key (e): ");
        long e = scanner.nextLong();

        // Calculate n and phi(n)
        long n = p * q;
        long phiN = (p - 1) * (q - 1);

        // Encrypt the message letter by letter
        long[] encryptedMessage = new long[message.length()];
        for (int i = 0; i < message.length(); i++) {
            long M = (long) message.charAt(i); // Convert character to ASCII value
            encryptedMessage[i] = squareAndMultiply(M, e, n); // Encrypt ASCII value
        }

        // Send values to the server
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            // Send p, q, e, and encrypted message to the server
            output.writeLong(p);
            output.writeLong(q);
            output.writeLong(e);
            output.writeObject(encryptedMessage);
            System.out.println("Encrypted message sent to server.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}