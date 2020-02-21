package tools.gitclient.config;

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
        public String name;
        public Type type;
        public String user;
        public String password;
    }

}
