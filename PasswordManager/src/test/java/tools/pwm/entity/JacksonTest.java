package tools.pwm.entity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {
    @Test
    public void serializeTest() {
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setTitle("sample01");
        item.setUrl("https://www.google.co.jp/");
        item.addKeyValues("user", "test-user");
        item.addKeyValues("password", "test-password");
        item.addKeyValues("secret", "test-secret");
        items.add(item);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(items);
            assertThat(json, containsString("\"title\":\"sample01\""));
            assertThat(json, containsString("\"url\":\"https://www.google.co.jp/\""));
            assertThat(json, containsString("\"keyValues\":[{\"key\":\"user\",\"value\":\"test-user\"}"));
            assertThat(json, containsString("{\"key\":\"user\",\"value\":\"test-user\"}"));
            assertThat(json, containsString("{\"key\":\"password\",\"value\":\"test-password\"}"));
            assertThat(json, containsString("{\"key\":\"secret\",\"value\":\"test-secret\"}"));

        } catch (JsonProcessingException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            fail();
        }
    }
}
