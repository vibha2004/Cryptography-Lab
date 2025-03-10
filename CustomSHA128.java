import java.util.Scanner;

public class CustomSHA128 {

    // Constants for the compression function
    private static final int[] K = {
            0x5A827999, 0x6ED9EBA1, 0x8F1BBCDC, 0xCA62C1D6
    };

    // Initial hash values (H0, H1, H2, H3)
    private static final int[] H = {
            0xA3B1BAC6, 0x56AA335C, 0x677D9197, 0xB27022DC
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Input 512-bit message (64 bytes)
        System.out.print("Enter a 512-bit (64-byte) message in hexadecimal: ");
        String input = scanner.nextLine().trim();

        // Validate input length (64 bytes = 128 hex characters)
        if (input.length() != 128) {
            System.out.println("Input must be exactly 128 hexadecimal characters (512 bits).");
            return;
        }

        // Step 2: Parse the input into 16 words (32 bits each)
        int[] M = new int[16];
        for (int i = 0; i < 16; i++) {
            String wordHex = input.substring(i * 8, (i + 1) * 8);
            M[i] = (int) Long.parseLong(wordHex, 16);
        }

        // Step 3: Message expansion (16 words -> 32 words)
        int[] W = new int[32];
        System.arraycopy(M, 0, W, 0, 16); // Copy first 16 words

        for (int i = 16; i < 32; i++) {
            W[i] = Integer.rotateLeft(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1);
        }

        // Step 4: Initialize hash values
        int A = H[0], B = H[1], C = H[2], D = H[3], E = 0;

        // Step 5: Compression function (32 rounds)
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

        // Step 6: Update hash values
        H[0] += A;
        H[1] += B;
        H[2] += C;
        H[3] += D;

        // Step 7: Truncate to 128-bit output
        String hash = String.format("%08x%08x%08x%08x", H[0], H[1], H[2], H[3]);

        // Output the final 128-bit hash
        System.out.println("SHA-128 Hash: " + hash);
    }
}