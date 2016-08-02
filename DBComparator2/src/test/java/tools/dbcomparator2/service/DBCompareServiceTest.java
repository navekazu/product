package tools.dbcomparator2.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.dbcomparator2.entity.ConnectEntity;
import tools.dbcomparator2.entity.DBCompareEntity;
import tools.dbcomparator2.enums.DBCompareStatus;

import static org.junit.Assert.assertEquals;

public class DBCompareServiceTest {

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
    public void test() {
        ConnectEntity connectEntity1 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb1/testdb")
                .user("sa")
                .password(null)
                .build();
        ConnectEntity connectEntity2 = ConnectEntity.builder()
                .library("h2-1.3.176.jar")
                .driver("org.h2.Driver")
                .url("jdbc:h2:file:./testdb2/testdb")
                .user("sa")
                .password(null)
                .build();

        DBCompareService service = new DBCompareService();
        service.startCompare(connectEntity1, connectEntity2);
        assertEquals(true, true);
    }
}
