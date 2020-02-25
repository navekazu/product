package tools.gitclient.config;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;


public class CredentialsConfigManager extends ConfigManagerBase {

    @Override
    public String getConfigName() {
        return "Credentials";
    }

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String INIT_VECTOR = "INIT_VECTOR";
    private IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes());

    public Credentials deserialize(String data) {
        Credentials credentials = new Credentials();
        return credentials;
    }

    public String serialize(Credentials credentials) {

        return "";
    }

    public static class Credentials {
        public enum Type {
            NONE,
            USER_PASSWORD,
        }
        public int no;
        public String name;
        public Type type;
        public String user;
        public String password;
        public String toString() {
            return String.format("%d,%s,%s,%s,%s", no, name, type.name(), user, password);
        }
    }

    public static String credentialsToBase64(Credentials credentials) {
        return String.format("%d,%s,%s,%s,%s",
                credentials.no, base64Encode(credentials.name), credentials.type.name(), base64Encode(credentials.user), base64Encode(credentials.password));
    }

    public static Credentials base64ToCredentials(String value) {
        Credentials credentials = new Credentials();
        String values[] = split(value, ",");
        credentials.no = Integer.parseInt(values[0]);
        credentials.name = base64Decode(values[1]);
        credentials.type = Credentials.Type.valueOf(values[2]);
        credentials.user = base64Decode(values[3]);
        credentials.password = base64Decode(values[4]);

        return credentials;
    }

    public static String[] split(String value, String delimiter) {
        if (value.length()==0) {
            return new String[0];
        }

        int from = 0;
        int to = 1;
        List<String> list = new ArrayList<>();

        while(from<=value.length()) {
            if (value.indexOf(delimiter, to)==-1) {
                to = value.length();
            } else {
                to = value.indexOf(delimiter, to);
            }
            list.add(value.substring(from, to));
            from = to+1;
            to = from+1;
        }

        return list.toArray(new String[list.size()]);
    }

    public static String base64Decode(String value) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] b = decoder.decode(value);
        return new String(b);
    }
    public static String base64Encode(String value) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(value.getBytes());
    }
}
