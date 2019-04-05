package info.studyup.studyupserver;

import org.junit.Before;

import com.gs.fw.common.mithra.test.ConnectionManagerForTests;
import com.gs.fw.common.mithra.test.MithraTestResource;

public class ReladomoConnectionTest {
    private String testRuntimeConfigXML = "MithraRuntimeConfig_test.xml";

    @Before
    public void setup() throws Exception {
        intializeReladomoForTest();
        initializeApp();
    }

    private void intializeReladomoForTest() {
        MithraTestResource testResource = new MithraTestResource(testRuntimeConfigXML);
        ConnectionManagerForTests connectionManager = ConnectionManagerForTests.getInstance("test");
        testResource.createSingleDatabase(connectionManager, "testconfig/SimpleBankTestData.txt");
        testResource.setUp();
    }
}
