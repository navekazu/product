package tools.gitclient.config;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.util.List;

import org.junit.Test;

public class ConfigManagerBaseTest {

    @Test
    public void addConfigNonDelimiterTest() {
        Test1ConfigManager config = new Test1ConfigManager();

        // ファイルは作られた？
        config.clearConfig();
        assertTrue(Files.exists(config.getConfigPath()));

        // デリミタのない項目は追加出来た？
        config.addConfig("configValue1");
        List<String> list = config.readConfig();
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is("configValue1"));

        // デリミタのない項目で値が違えば追加
        config.addConfig("configValue2");
        list = config.readConfig();
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is("configValue1"));
        assertThat(list.get(1), is("configValue2"));
        config.addConfig("configValue3");
        list = config.readConfig();
        assertThat(list.size(), is(3));
        assertThat(list.get(0), is("configValue1"));
        assertThat(list.get(1), is("configValue2"));
        assertThat(list.get(2), is("configValue3"));

        // デリミタのない項目で同じ値を入れたら、後ろに回される
        config.addConfig("configValue1");
        list = config.readConfig();
        assertThat(list.size(), is(3));
        assertThat(list.get(0), is("configValue2"));
        assertThat(list.get(1), is("configValue3"));
        assertThat(list.get(2), is("configValue1"));
    }

    @Test
    public void addConfigDelimiterTest() {
        Test2ConfigManager config = new Test2ConfigManager();

        // ファイルは作られた？
        config.clearConfig();
        assertTrue(Files.exists(config.getConfigPath()));

        // デリミタ付き項目は追加出来た？
        config.addConfig("config1=configValue1");
        List<String> list = config.readConfig();
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is("config1=configValue1"));

        // デリミタ付き項目で値が違えば追加
        config.addConfig("config2=configValue2");
        list = config.readConfig();
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is("config1=configValue1"));
        assertThat(list.get(1), is("config2=configValue2"));
        config.addConfig("config3=configValue3");
        list = config.readConfig();
        assertThat(list.size(), is(3));
        assertThat(list.get(0), is("config1=configValue1"));
        assertThat(list.get(1), is("config2=configValue2"));
        assertThat(list.get(2), is("config3=configValue3"));

        // デリミタ付き項目で同じ値を入れたら、後ろに回される
        config.addConfig("config1=configValue1_");
        list = config.readConfig();
        assertThat(list.size(), is(3));
        assertThat(list.get(0), is("config2=configValue2"));
        assertThat(list.get(1), is("config3=configValue3"));
        assertThat(list.get(2), is("config1=configValue1_"));
    }

    private static class Test1ConfigManager extends ConfigManagerBase {
        @Override
        public String getConfigName() {
            return "Test1";
        }

    }
    private static class Test2ConfigManager extends ConfigManagerBase {
        @Override
        public String getConfigName() {
            return "Test2";
        }

    }

    @Test
    public void splitTest1() {
        String[] values = CredentialsConfigManager.split("0,1,2,3", ',');

        assertThat(values.length, is(4));
        assertThat(values[0], is("0"));
        assertThat(values[1], is("1"));
        assertThat(values[2], is("2"));
        assertThat(values[3], is("3"));
    }

    @Test
    public void splitTest2() {
        String[] values = CredentialsConfigManager.split("abc,def,ghi,jkl", ',');

        assertThat(values.length, is(4));
        assertThat(values[0], is("abc"));
        assertThat(values[1], is("def"));
        assertThat(values[2], is("ghi"));
        assertThat(values[3], is("jkl"));
    }

    @Test
    public void splitTest3() {
        String[] values = CredentialsConfigManager.split("0,1,,", ',');

        assertThat(values.length, is(4));
        assertThat(values[0], is("0"));
        assertThat(values[1], is("1"));
        assertThat(values[2], is(""));
        assertThat(values[3], is(""));
    }

    @Test
    public void splitTest4() {
        String[] values = CredentialsConfigManager.split("0,1,,3", ',');

        assertThat(values.length, is(4));
        assertThat(values[0], is("0"));
        assertThat(values[1], is("1"));
        assertThat(values[2], is(""));
        assertThat(values[3], is("3"));
    }
}
