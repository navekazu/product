package tools.gitclient.config;

public class RepositoryCredentialsConfigManager extends ConfigManagerBase {

    @Override
    public String getConfigName() {
        // TODO 自動生成されたメソッド・スタブ
        return "RepositoryCredentials";
    }

    public RepositoryCredentials deserialize(String data) {
        return base64ToRepositoryCredentials(data);
    }

    public String serialize(RepositoryCredentials credentials) {
        return repositoryCredentialsToBase64(credentials);
    }

    public static class RepositoryCredentials {
        public String repository;
        public long credentials;
        public String toString() {
            return String.format("%s,%d", repository, credentials);
        }
    }

    public static String repositoryCredentialsToBase64(RepositoryCredentials credentials) {
        return String.format("%s,%d",
                base64Encode(credentials.repository), credentials.credentials);
    }

    public static RepositoryCredentials base64ToRepositoryCredentials(String value) {
        RepositoryCredentials credentials = new RepositoryCredentials();
        String values[] = split(value, ',');
        credentials.repository = base64Decode(values[0]);
        credentials.credentials = Long.parseLong(values[1]);

        return credentials;
    }
}
