package tools.mailer.plugin;

import org.junit.*;
import tools.mailer.di.container.DIContainer;
import tools.mailer.entity.Account;
import tools.mailer.entity.MailAddress;
import tools.mailer.entity.Message;
import tools.mailer.entity.Server;

import java.util.Date;

public class SendMailPluginTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
        DIContainer diContainer = DIContainer.getInstance();
        diContainer.loadPlugin();
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
    public void recvMailTest() throws Exception {
        SendMailPlugin sendMailPlugin = new SendMailPlugin();
        Account account = Account.builder()
                .accountName("Test")
                .mailAddress(
                        MailAddress.builder()
                                .userName("Test")
                                .localName("test")
                                .domainName("localhost")
                                .password("test")
                                .build()
                )
                .pop3Server(
                        Server.builder()
                                .name("localhost")
                                .port(110)
                                .build()
                )
                .smtpServer(
                        Server.builder()
                                .name("localhost")
                                .port(25)
                                .build()
                )
                .build();

        Message message = Message.builder()
                .from(account.getMailAddress())
                .to(new MailAddress[]{account.getMailAddress()})
                .sentDate(new Date())
                .subject("test subject.")
                .text("message body.")
                .build();

        sendMailPlugin.sendMail(account, message);
        sendMailPlugin.recvMail(account);
    }
}
