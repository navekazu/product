package tools.dbcomparator2.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
    public static String getHash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(value.getBytes());

            StringBuilder builder = new StringBuilder();
            for (byte b : md.digest()) {
                String hex = String.format("%02x", b);
                builder.append(hex);
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
