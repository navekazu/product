package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import tools.dbcomparator2.util.StringUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PrimaryKeyValue {
    private Map<String, String> primaryKeyValue = new HashMap<>();
    private String primaryKeyHash;
    private String primaryKeyValueHash;

    public void addPrimaryKeyValue(String key, String value) {
        primaryKeyValue.put(key, value);
    }

    public String getPrimaryKeyValue(String key) {
        return primaryKeyValue.get(key);
    }

    public int getPrimaryKeyCount() {
        return primaryKeyValue.keySet().size();
    }

    public void createPrimaryKeyHash() {
        StringBuilder builderKey = new StringBuilder();
        StringBuilder builderValue = new StringBuilder();

        primaryKeyValue.keySet().stream()
                .forEach(v -> {
                    builderKey.append(v);
                    builderValue.append(primaryKeyValue.get(v));
                });

        primaryKeyHash = StringUtil.getHash(builderKey.toString());
        primaryKeyValueHash = StringUtil.getHash(builderValue.toString());
    }
}
