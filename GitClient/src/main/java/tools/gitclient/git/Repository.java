package tools.gitclient.git;

import java.io.File;
import java.net.URL;

public class Repository {
    private String name;
    private URL remote;
    private File local;

    public Repository() {

    }
    public static RepositoryBuilder builder() {
        return new RepositoryBuilder();
    }

    public static class RepositoryBuilder {
        private String name;
        private URL remote;
        private File local;

        public RepositoryBuilder name(String name) {
            this.name = name;
            return this;
        }
        public RepositoryBuilder remote(URL remote) {
            this.remote = remote;
            return this;
        }
        public RepositoryBuilder local(File local) {
            this.local = local;
            return this;
        }

        public Repository build() {
            Repository repository = new Repository();
            repository.name = name;
            repository.remote = remote;
            repository.local = local;
            return repository;
        }
    }

    public String getName() {
        return name;
    }

    public URL getRemote() {
        return remote;
    }

    public File getLocal() {
        return local;
    }
}
