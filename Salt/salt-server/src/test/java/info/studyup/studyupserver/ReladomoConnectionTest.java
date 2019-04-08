package info.studyup.studyupserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gs.fw.common.mithra.test.ConnectionManagerForTests;
import com.gs.fw.common.mithra.test.MithraTestResource;

public class ReladomoConnectionTest {
    private String testRuntimeConfigXML = "MithraRuntimeConfig_test.xml";
    private MithraTestResource testResource;

    @Before
    public void setup() throws Exception {
        intializeReladomoForTest();
        // initializeApp();
    }

    @After
    public void tearDown() throws Exception {
        testResource.tearDown();
    }

    private void intializeReladomoForTest() throws Exception {
        testResource = new MithraTestResource(testRuntimeConfigXML);
        ConnectionManagerForTests connectionManager = ConnectionManagerForTests.getInstance("test_db");
        testResource.createSingleDatabase(connectionManager, "testconfig/SimpleBankTestData.txt");
        testResource.createDatabaseForStringSourceAttribute(connectionManager, "deskA", "mithraTestDataForDesk.txt");
        testResource.setUp();
    }

    @Test
    public void test() {

    }
}
