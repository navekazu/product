package tools.dbcomparator2;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
    public void streamSpeedTest() {
        long start, end;
        List<Integer> dataList = new ArrayList<>();
        IntStream.range(0, 10000000).forEach(dataList::add);

        // parallelStream
        start = System.currentTimeMillis();
        dataList.parallelStream().forEach(i -> i=i+1);
        end = System.currentTimeMillis();
        System.out.println("parallelStream:"+(end-start));

        // stream
        start = System.currentTimeMillis();
        dataList.parallelStream().forEach(i -> i=i+1);
        end = System.currentTimeMillis();
        System.out.println("stream:"+(end-start));

        assertEquals(true, true);
    }
}
