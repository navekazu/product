package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RecordHashEntity {
    private String primaryKeyHashValue;
    private String allColumnHashValue;

    public static String createHashValue(List<String> list) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            for (String value: list) {
                messageDigest.update(value.getBytes());
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b: messageDigest.digest()) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
