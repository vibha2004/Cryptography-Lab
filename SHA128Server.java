import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SHA128Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("SHA-128 Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Handle client request in a new thread
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (Scanner in = new Scanner(socket.getInputStream());
                 Scanner scanner = new Scanner(System.in)) {

                // Read the 512-bit input from the client
                String input = in.nextLine();
                System.out.println("Received input from client: " + input);

                // Compute the SHA-128 hash
                String hash = computeSHA128(input);

                // Send the hash back to the client
                socket.getOutputStream().write((hash + "\n").getBytes());
                System.out.println("Sent hash to client: " + hash);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private String computeSHA128(String input) {
            // Constants for the compression function
            int[] K = {0x5A827999, 0x6ED9EBA1, 0x8F1BBCDC, 0xCA62C1D6};

            // Initial hash values (H0, H1, H2, H3)
            int[] H = {0xA3B1BAC6, 0x56AA335C, 0x677D9197, 0xB27022DC};

            // Parse the input into 16 words (32 bits each)
            int[] M = new int[16];
            for (int i = 0; i < 16; i++) {
                String wordHex = input.substring(i * 8, (i + 1) * 8);
                M[i] = (int) Long.parseLong(wordHex, 16);
            }

            // Message expansion (16 words -> 32 words)
            int[] W = new int[32];
            System.arraycopy(M, 0, W, 0, 16); // Copy first 16 words

            for (int i = 16; i < 32; i++) {
                W[i] = Integer.rotateLeft(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1);
            }

            // Initialize hash values
            int A = H[0], B = H[1], C = H[2], D = H[3], E = 0;

            // Compression function (32 rounds)
            for (int i = 0; i < 32; i++) {
                int F, K_i;

                if (i < 8) {
                    F = (B & C) | (~B & D);
                    K_i = K[0];
                } else if (i < 16) {
                    F = B ^ C ^ D;
                    K_i = K[1];
                } else if (i < 24) {
                    F = (B & C) | (B & D) | (C & D);
                    K_i = K[2];
                } else {
                    F = B ^ C ^ D;
                    K_i = K[3];
                }

                int TEMP = Integer.rotateLeft(A, 5) + F + E + K_i + W[i];
                E = D;
                D = C;
                C = Integer.rotateLeft(B, 30);
                B = A;
                A = TEMP;
            }

            // Update hash values
            H[0] += A;
            H[1] += B;
            H[2] += C;
            H[3] += D;

            // Truncate to 128-bit output
            return String.format("%08x%08x%08x%08x", H[0], H[1], H[2], H[3]);
        }
    }
}