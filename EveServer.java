import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EveServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Eve (Server) is listening on port " + PORT);

            // Take q and alpha from Eve
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter prime number (q): ");
            int q = scanner.nextInt();
            System.out.print("Enter primitive root (alpha): ");
            int alpha = scanner.nextInt();

            // Take Eve's private keys
            System.out.print("Enter Eve's private key for Alice (Xd1): ");
            int Xd1 = scanner.nextInt();
            System.out.print("Enter Eve's private key for Bob (Xd2): ");
            int Xd2 = scanner.nextInt();

            // Compute Eve's public keys
            int Yd1 = (int) (Math.pow(alpha, Xd1) % q);
            int Yd2 = (int) (Math.pow(alpha, Xd2) % q);
            System.out.println("Eve's public key for Alice (Yd1): " + Yd1);
            System.out.println("Eve's public key for Bob (Yd2): " + Yd2);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // Handle client request in a new thread
                new Thread(new ClientHandler(socket, q, alpha, Yd1, Yd2, Xd1, Xd2)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private int q;
        private int alpha;
        private int Yd1;
        private int Yd2;
        private int Xd1;
        private int Xd2;

        public ClientHandler(Socket socket, int q, int alpha, int Yd1, int Yd2, int Xd1, int Xd2) {
            this.socket = socket;
            this.q = q;
            this.alpha = alpha;
            this.Yd1 = Yd1;
            this.Yd2 = Yd2;
            this.Xd1 = Xd1;
            this.Xd2 = Xd2;
        }

        @Override
        public void run() {
            try (Scanner in = new Scanner(socket.getInputStream());
                 Scanner scanner = new Scanner(System.in)) {

                // Read the client's name
                String clientName = in.nextLine();
                System.out.println(clientName + " has connected.");

                // Send q and alpha to the client
                socket.getOutputStream().write((q + "\n" + alpha + "\n").getBytes());

                // Read the public key from the client
                int publicKey = in.nextInt();
                System.out.println("Received public key from " + clientName + ": " + publicKey);

                if (clientName.equals("Alice")) {
                    // Send Yd2 to Alice
                    socket.getOutputStream().write((Yd2 + "\n").getBytes());
                    System.out.println("Sent Yd2 to Alice: " + Yd2);

                    // Compute K2 = Ya^Xd2 mod q (Eve's shared key with Alice)
                    int K2 = (int) (Math.pow(publicKey, Xd2) % q);
                    System.out.println("Eve computed shared key with Alice (K2): " + K2);
                } else if (clientName.equals("Bob")) {
                    // Send Yd1 to Bob
                    socket.getOutputStream().write((Yd1 + "\n").getBytes());
                    System.out.println("Sent Yd1 to Bob: " + Yd1);

                    // Compute K1 = Yb^Xd1 mod q (Eve's shared key with Bob)
                    int K1 = (int) (Math.pow(publicKey, Xd1) % q);
                    System.out.println("Eve computed shared key with Bob (K1): " + K1);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}