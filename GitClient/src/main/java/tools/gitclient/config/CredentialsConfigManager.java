package tools.gitclient.config;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class CredentialsConfigManager extends ConfigManagerBase {

    @Override
    public String getConfigName() {
        return "Credentials";
    }

    public Credentials deserialize(String data) {
        return base64ToCredentials(data);
    }

    public String serialize(Credentials credentials) {
        return credentialsToBase64(credentials);
    }

    public static class Credentials {
        public enum Type {
            NONE,
            USER_PASSWORD,
        }
        public long no;
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
        String values[] = split(value, ',');
        credentials.no = Long.parseLong(values[0]);
        credentials.name = base64Decode(values[1]);
        credentials.type = Credentials.Type.valueOf(values[2]);
        credentials.user = base64Decode(values[3]);
        credentials.password = base64Decode(values[4]);

        return credentials;
    }

    public static CredentialsProvider createCredentialsProvider(Credentials c) {
        switch(c.type) {
        case NONE:
            return null;

        case USER_PASSWORD:
            return  new UsernamePasswordCredentialsProvider(c.user, c.password);
        }

        return null;
    }
}
