package tools.pwm.entity;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String title;
    private String url;
    private List<KeyValue> keyValues;

    public void addKeyValues(String key, String value) {
        if (keyValues == null) {
            keyValues = new ArrayList<>();
        }
        keyValues.add(new KeyValue(key, value));
    }

    public KeyValue getKeyValue(String key) {
        if (keyValues == null) {
            return null;
        }

        for (KeyValue keyValue: keyValues) {
            if (keyValue.getKey().equals(key)) {
                return keyValue;
            }
        }
        return null;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<KeyValue> getKeyValues() {
        return keyValues;
    }
    public void setKeyValues(List<KeyValue> keyValues) {
        this.keyValues = keyValues;
    }


}
