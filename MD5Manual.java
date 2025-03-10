
import java.util.Scanner;

public class MD5Manual {
    private static final int A = 0x67452301;
    private static final int B = 0xefcdab89;
    private static final int C = 0x98badcfe;
    private static final int D = 0x10325476;

    private static final int[] S = {
        7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
        5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
        4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
        6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    };

    private static final int[] K = new int[64];

    static {
        for (int i = 0; i < 64; i++) {
            K[i] = (int) (long) ((1L << 32) * Math.abs(Math.sin(i + 1)));
        }
    }

    private int[] buffer = {A, B, C, D};
    private long size;

    private static int leftRotate(int x, int n) {
        return (x << n) | (x >>> (32 - n));
    }

    public void update(byte[] input) {
        size += input.length * 8L;
        byte[] padded = padInput(input);
        processBlocks(padded);
    }

    private byte[] padInput(byte[] input) {
        int newLength = ((input.length + 8) / 64 + 1) * 64;
        byte[] padded = new byte[newLength];
        System.arraycopy(input, 0, padded, 0, input.length);
        padded[input.length] = (byte) 0x80;
        
        for (int i = 0; i < 8; i++) {
            padded[newLength - 8 + i] = (byte) ((size >>> (8 * i)) & 0xFF);
        }
        return padded;
    }

    private void processBlocks(byte[] data) {
        int[] words = new int[16];
        for (int i = 0; i < data.length; i += 64) {
            for (int j = 0; j < 16; j++) {
                words[j] = ((data[i + j * 4] & 0xFF)) |
                           ((data[i + j * 4 + 1] & 0xFF) << 8) |
                           ((data[i + j * 4 + 2] & 0xFF) << 16) |
                           ((data[i + j * 4 + 3] & 0xFF) << 24);
            }
            md5Step(words);
        }
    }

    private void md5Step(int[] input) {
        int AA = buffer[0], BB = buffer[1], CC = buffer[2], DD = buffer[3];
        int E, j;

        for (int i = 0; i < 64; i++) {
            if (i < 16) { E = (BB & CC) | (~BB & DD); j = i; }
            else if (i < 32) { E = (BB & DD) | (CC & ~DD); j = (5 * i + 1) % 16; }
            else if (i < 48) { E = BB ^ CC ^ DD; j = (3 * i + 5) % 16; }
            else { E = CC ^ (BB | ~DD); j = (7 * i) % 16; }

            int temp = DD;
            DD = CC;
            CC = BB;
            BB = BB + leftRotate(AA + E + K[i] + input[j], S[i]);
            AA = temp;
        }

        buffer[0] += AA;
        buffer[1] += BB;
        buffer[2] += CC;
        buffer[3] += DD;
    }

    public String digest() {
        StringBuilder result = new StringBuilder();
        for (int i : buffer) {
            result.append(String.format("%08x", Integer.reverseBytes(i)));
        }
        return result.toString();
    }

    public static String computeMD5(String input) {
        MD5Manual md5 = new MD5Manual();
        md5.update(input.getBytes());
        return md5.digest();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        
        scanner.close();
    }
}