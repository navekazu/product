package tools.mailer.persistence;

import org.junit.*;
import tools.mailer.entity.Account;
import tools.mailer.entity.MailAddress;
import tools.mailer.entity.Server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountSerializerTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
        SerializerBase.setTestMode();
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
    public void getPersistenceFileTest() throws Exception {
        AccountSerializer accountSerializer = new AccountSerializer();
        Path path = accountSerializer.getPersistenceFile();
        assertEquals(Paths.get(System.getProperty("user.home"), ".mailer_test", accountSerializer.persistenceName()+".json"), path);
    }

//    @Test
//    public void getPersistenceDirectoryTest() throws Exception {
//        AccountSerializer accountSerializer = new AccountSerializer();
//        Path path = accountSerializer.getPersistenceDirectory();
//        assertEquals(Paths.get(System.getProperty("user.home"), ".mailer_test", accountSerializer.persistenceName()), path);
//    }

    @Test
    public void writeAllTest() throws Exception {
        Account account1 = Account.builder()
                .accountName("test1")
                .mailAddress(
                        MailAddress.builder()
                                .userName("Test1")
                                .localName("test1")
                                .domainName("localhost")
                                .password("test1")
                                .build()
                )
                .pop3Server(
                        Server.builder()
                                .name("localhost1")
                                .port(110)
                                .build()
                )
                .smtpServer(
                        Server.builder()
                                .name("localhost1")
                                .port(25)
                                .build()
                )
                .build();
        Account account2 = Account.builder()
                .accountName("test2")
                .mailAddress(
                        MailAddress.builder()
                                .userName("Test2")
                                .localName("test2")
                                .domainName("localhost")
                                .password("test2")
                                .build()
                )
                .pop3Server(
                        Server.builder()
                                .name("localhost2")
                                .port(110)
                                .build()
                )
                .smtpServer(
                        Server.builder()
                                .name("localhost2")
                                .port(25)
                                .build()
                )
                .build();
        List<Account> list1 = new ArrayList<>();
        list1.add(account1);
        list1.add(account2);

        AccountSerializer accountSerializer = new AccountSerializer();
        accountSerializer.writeAll(list1);
        List<Account> list2 = accountSerializer.readAll();

        // 書いた内容を読んだ結果が同じか？
        assertEquals(list1, list2);
    }

}
