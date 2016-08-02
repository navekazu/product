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

public class DBParseServiceTest {

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
        DBCompareEntity dbCompareEntity = DBCompareEntity.builder()
                .status(DBCompareStatus.READY)
                .connectEntity(
                        ConnectEntity.builder()
                                .library("")
                                .driver("")
                                .url("")
                                .user("")
                                .password("")
                                .build()
                )
                .build();

        DBParseService service = new DBParseService();
        service.startParse(dbCompareEntity);
        assertEquals(true, true);
    }
}
