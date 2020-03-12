package tools.gitclient.config;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RepositoryCredentialsConfigManagerTest {
    @Test
    public void repositoryCredentialsSerializeTest() {
        RepositoryCredentialsConfigManager config = new RepositoryCredentialsConfigManager();
        RepositoryCredentialsConfigManager.RepositoryCredentials credentials = new RepositoryCredentialsConfigManager.RepositoryCredentials();
        credentials.repository = "aaa";
        credentials.credentials= 1;
        assertThat(config.serialize(credentials), is("YWFh,1"));
    }

    @Test
    public void repositoryCredentialsDeserializeTest() {
        RepositoryCredentialsConfigManager config = new RepositoryCredentialsConfigManager();
        RepositoryCredentialsConfigManager.RepositoryCredentials credentials = config.deserialize("YWFh,1");
        assertThat(credentials.repository, is("aaa"));
        assertThat(credentials.credentials, is(1L));
    }

}
