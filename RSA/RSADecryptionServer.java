import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RSADecryptionServer {

    // Extended Euclidean Algorithm to find modular inverse
    public static long extendedEuclidean(long a, long b) {
        long A1 = 1, A2 = 0, A3 = a;
        long B1 = 0, B2 = 1, B3 = b;

        while (B3 != 1) {
            long Q = A3 / B3;
            long T1 = A1 - Q * B1;
            long T2 = A2 - Q * B2;
            long T3 = A3 - Q * B3;

            A1 = B1;
            A2 = B2;
            A3 = B3;
            B1 = T1;
            B2 = T2;
            B3 = T3;
        }

        return (B2 + a) % a; // Ensure the result is positive
    }

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
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

                    // Receive values from the client
                    long p = input.readLong();
                    long q = input.readLong();
                    long e = input.readLong();
                    long[] encryptedMessage = (long[]) input.readObject();

                    System.out.println("Received values from client:");
                    System.out.println("p: " + p);
                    System.out.println("q: " + q);
                    System.out.println("e: " + e);
                    System.out.println("Encrypted Message: ");
                    for (long c : encryptedMessage) {
                        System.out.print(c + " ");
                    }
                    System.out.println();

                    // Calculate n and phi(n)
                    long n = p * q;
                    long phiN = (p - 1) * (q - 1);

                    // Calculate private key (d) using extended Euclidean algorithm
                    long d = extendedEuclidean(phiN, e);
                    System.out.println("Private Key (d): " + d);

                    // Decrypt the message letter by letter
                    StringBuilder decryptedMessage = new StringBuilder();
                    for (long c : encryptedMessage) {
                        long M = squareAndMultiply(c, d, n); // Decrypt ASCII value
                        decryptedMessage.append((char) M); // Convert ASCII to character
                    }

                    System.out.println("Decrypted Message: " + decryptedMessage.toString());

                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}