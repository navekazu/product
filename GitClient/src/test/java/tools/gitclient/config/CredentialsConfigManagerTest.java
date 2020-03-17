package tools.gitclient.config;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class CredentialsConfigManagerTest {


    @Test
    public void credentialsTest() {
        CredentialsConfigManager.Credentials credentials = new CredentialsConfigManager.Credentials();
        credentials.no = 1;
        credentials.name = "name";
        credentials.type = CredentialsConfigManager.Credentials.Type.USER_PASSWORD;
        credentials.user = "user";
        credentials.password = "password";
        assertThat(credentials.toString(), is("1,name,USER_PASSWORD,user,password"));
    }

    @Test
    public void base64Test() {
        assertThat(CredentialsConfigManager.base64Encode("abc"), is("YWJj"));
        assertThat(CredentialsConfigManager.base64Encode("日本語"), is("5pel5pys6Kqe"));
        assertThat(CredentialsConfigManager.base64Encode("comma,カンマ"), is("Y29tbWEs44Kr44Oz44Oe"));

        assertThat(CredentialsConfigManager.base64Decode("YWJj"), is("abc"));
        assertThat(CredentialsConfigManager.base64Decode("5pel5pys6Kqe"), is("日本語"));
        assertThat(CredentialsConfigManager.base64Decode("Y29tbWEs44Kr44Oz44Oe"), is("comma,カンマ"));
    }

    @Test
    public void credentialsSerializeTest() {
        CredentialsConfigManager.Credentials credentials = new CredentialsConfigManager.Credentials();
        credentials.no = 1;
        credentials.name = "name";
        credentials.type = CredentialsConfigManager.Credentials.Type.USER_PASSWORD;
        credentials.user = "user";
        credentials.password = "password";

        assertThat(CredentialsConfigManager.credentialsToBase64(credentials),
                is("1,bmFtZQ==,USER_PASSWORD,dXNlcg==,cGFzc3dvcmQ="));

        credentials = CredentialsConfigManager.base64ToCredentials("1,bmFtZQ==,USER_PASSWORD,dXNlcg==,cGFzc3dvcmQ=");
        assertThat(credentials.no, is(1L));
        assertThat(credentials.name, is("name"));
        assertThat(credentials.type, is(CredentialsConfigManager.Credentials.Type.USER_PASSWORD));
        assertThat(credentials.user, is("user"));
        assertThat(credentials.password, is("password"));


        credentials = new CredentialsConfigManager.Credentials();
        credentials.no = 1;
        credentials.name = "name";
        credentials.type = CredentialsConfigManager.Credentials.Type.NONE;
        credentials.user = "";
        credentials.password = "";

        assertThat(CredentialsConfigManager.credentialsToBase64(credentials),
                is("1,bmFtZQ==,NONE,,"));

        credentials = CredentialsConfigManager.base64ToCredentials("1,bmFtZQ==,NONE,,");
        assertThat(credentials.no, is(1L));
        assertThat(credentials.name, is("name"));
        assertThat(credentials.type, is(CredentialsConfigManager.Credentials.Type.NONE));
        assertThat(credentials.user, is(""));
        assertThat(credentials.password, is(""));
    }
}
