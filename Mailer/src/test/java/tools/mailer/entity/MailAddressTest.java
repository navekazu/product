package tools.mailer.entity;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MailAddressTest {
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
    public void toMailAddress_toFullMailAddressTest() throws Exception {
        MailAddress mailAddress;

        // スタンダード
        mailAddress = MailAddress.builder().userName("Test User").localName("test").domainName("domain").build();
        assertEquals("test@domain", mailAddress.toMailAddress());
        assertEquals("Test User <test@domain>", mailAddress.toFullMailAddress());

        // ユーザー名にカンマあり → ダブルクォートでエスケープされる？
        mailAddress = MailAddress.builder().userName("Test, User").localName("test").domainName("domain").build();
        assertEquals("test@domain", mailAddress.toMailAddress());
        assertEquals("\"Test, User\" <test@domain>", mailAddress.toFullMailAddress());

        // userNameなし
        mailAddress = MailAddress.builder().localName("test").domainName("domain").build();
        assertEquals("test@domain", mailAddress.toMailAddress());
        assertEquals("test@domain", mailAddress.toFullMailAddress());

        // localNameなし
        mailAddress = MailAddress.builder().userName("Test User").domainName("domain").build();
        assertEquals("@domain", mailAddress.toMailAddress());
        assertEquals("Test User <@domain>", mailAddress.toFullMailAddress());

        // domainNameなし
        mailAddress = MailAddress.builder().userName("Test User").localName("test").build();
        assertEquals("test@", mailAddress.toMailAddress());
        assertEquals("Test User <test@>", mailAddress.toFullMailAddress());
    }
}
