package tools.directorymirroringtool.controller;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MirroringRowControllerTest {
    @Test
    public void getIntervalStringTest() throws Exception {
        assertEquals("1 minute.",  MirroringRowController.getIntervalString(1));     // 1分は分に複数のsが付かない
        assertEquals("2 minutes.", MirroringRowController.getIntervalString(2));     // 2分は分に複数のsが付く
        assertEquals("1 hour.", MirroringRowController.getIntervalString(60));       // 1時間は時間に複数のsが付かない
        assertEquals("2 hours.", MirroringRowController.getIntervalString(120));     // 2時間は時間に複数のsが付く

        assertEquals("1 hour 1 minute.", MirroringRowController.getIntervalString(61));     // 単数時間、単数分
        assertEquals("1 hour 2 minutes.", MirroringRowController.getIntervalString(62));     // 単数時間、複数分
        assertEquals("2 hours 1 minute.", MirroringRowController.getIntervalString(121));     // 複数時間、単数分
        assertEquals("2 hours 2 minutes.", MirroringRowController.getIntervalString(122));     // 複数時間、複数分



    }

/*

    @Test
    public void splitQueryTest() {
        String sql;
        String[] sqls;

        sql = "select * from foo";
        sqls = splitQuery(sql);
        assertEquals(1, sqls.length);


        sql = "select * from foo;\nselect * from foo;";
        sqls = splitQuery(sql);
        assertEquals(2, sqls.length);

        sql = "select * from foo;\nselect * from foo;\nselect * from foo;";
        sqls = splitQuery(sql);
        assertEquals(3, sqls.length);

        sql = "select * from foo/\nselect * from foo/";
        sqls = splitQuery(sql);
        assertEquals(2, sqls.length);

        sql = "select * from foo/\nselect * from foo/\nselect * from foo/";
        sqls = splitQuery(sql);
        assertEquals(3, sqls.length);


        sql = "select * from foo\n;\nselect * from foo\n;";
        sqls = splitQuery(sql);
        assertEquals(2, sqls.length);

        sql = "select * from foo\n;\nselect * from foo\n;\nselect * from foo\n;";
        sqls = splitQuery(sql);
        assertEquals(3, sqls.length);

        sql = "select * from foo\n/\nselect * from foo\n/";
        sqls = splitQuery(sql);
        assertEquals(2, sqls.length);

        sql = "select * from foo\n/\nselect * from foo\n/\nselect * from foo\n/";
        sqls = splitQuery(sql);
        assertEquals(3, sqls.length);


        sql = "select * from foo; \nselect * from foo; ";
        sqls = splitQuery(sql);
        assertEquals(2, sqls.length);

        sql = "select * from foo;  \nselect * from foo;  \nselect * from foo;";
        sqls = splitQuery(sql);
        assertEquals(3, sqls.length);

        sql = "select * from foo/ \nselect * from foo/";
        sqls = splitQuery(sql);
        assertEquals(2, sqls.length);

        sql = "select * from foo/   \nselect * from foo/   \nselect * from foo/";
        sqls = splitQuery(sql);
        assertEquals(3, sqls.length);


    }
    String[] splitQuery(String sql) {
//        String[] split = sql.trim().split("(;\n|/\n)");
        return sql.trim().split("(;\\s*\n|/\\s*\n)");
    }
*/
}
