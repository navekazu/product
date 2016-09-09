package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * レコード情報
 */
@Data
@Builder
public class RecordHashEntity {
    // プライマリキーの値のハッシュ値
    private String primaryKeyHashValue;

    // 全カラムの値のハッシュ値
    private String allColumnHashValue;

    // プライマリーキーのカラム名とその値のマップ
    private Map<String, Object> primaryKeyValueMap;

    public void putPrimaryKeyValueMap(String column, Object value) {
        if (primaryKeyValueMap==null) {
            primaryKeyValueMap = new HashMap<>();
        }
        primaryKeyValueMap.put(column, value);
    }

    public static String createHashValue(List<Object> list) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            for (Object value: list) {
                messageDigest.update((value == null ? "".getBytes() : value.toString().getBytes()));
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
