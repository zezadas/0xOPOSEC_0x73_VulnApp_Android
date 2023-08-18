package pt.oposec.vulnapp.helpers;

public class Encryption {
    public static String xor(String word, String secret_str) {
        byte[] secret = secret_str.getBytes();
        byte[] input = word.getBytes();
        final byte[] output = new byte[input.length];
        if (secret.length == 0) {
            throw new IllegalArgumentException("empty security key");
        }
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            ++spos;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return new String(output);
    }
}
