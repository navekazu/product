package tools.addressprinter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.*;
import tools.addressprinter.entity.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AppTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
    }

    @AfterClass
    public static void afterClass() throws Exception {
    }

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void jacksonTest() throws IOException {
        Data data = new Data();
        data.font = new Font();
        data.font.name = "Font name";
        data.font.size = 1;
        data.font.bold = true;
        data.font.italic = false;
        data.font.unberline = false;

        Address address = new Address();
        address.zipNo = new ZipNo();
        address.zipNo.sectionNo = "100";
        address.zipNo.cityNo = "0001";
        address.address1 = "東京都";
        address.address2 = "千代田区";
        address.address3 = "千代田1-2-3";
        address.useFamilyNameForEveryone = false;

        Person person1 = new Person();
        person1.familyName = "千代";
        person1.name = "千代助";

        Person person2 = new Person();
        person2.familyName = "千代";
        person2.name = "千代美";

        address.personList = new ArrayList<>();
        address.personList.add(person1);
        address.personList.add(person2);

        data.addressList = new ArrayList<>();
        data.addressList.add(address);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonValue = mapper.writeValueAsString(data);

        Data data2 = mapper.readValue(jsonValue, Data.class);

        assertEquals(data, data2);
    }
}
