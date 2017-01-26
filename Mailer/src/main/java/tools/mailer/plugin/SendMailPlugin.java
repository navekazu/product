package tools.mailer.plugin;

import com.sun.mail.pop3.POP3Store;
import tools.mailer.di.anntation.*;
import tools.mailer.di.anntation.Process;
import tools.mailer.entity.Account;
import tools.mailer.entity.Message;
import tools.mailer.processor.MailProcessor;

import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Properties;

@Plugin
public class SendMailPlugin {
    @Autowired
    public MailProcessor mailProcessor;

    @Process(processType = ProcessType.SEND_MAIL)
    public void sendMail(Account account, Message message) {
        System.out.println("sendMail");

        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", account.getSmtpServer().getName());

        Session session = Session.getInstance(props, null);
        //session.setDebug(debug);


        try {
            javax.mail.Address[] javaMailAddresses = new javax.mail.Address[message.getTo().length];
            for (int i=0; i<javaMailAddresses.length; i++) {
                InternetAddress internetAddress = new InternetAddress(message.getTo()[i].toFullMailAddress());
                javaMailAddresses[i] = internetAddress;
            }

            javax.mail.Message javaMailMessage = new MimeMessage(session);
            javaMailMessage.setFrom(new InternetAddress(message.getFrom().toFullMailAddress()));
            javaMailMessage.setRecipients(javax.mail.Message.RecipientType.TO, javaMailAddresses);
            javaMailMessage.setSubject(message.getSubject());
            javaMailMessage.setSentDate(message.getSentDate());
            javaMailMessage.setText(message.getText());

            javax.mail.Transport.send(javaMailMessage);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
    }

    @Process(processType=ProcessType.RECV_MAIL)
    public void recvMail(Account account) {
        System.out.println("recvMail");

        Properties props = new Properties();
        props.setProperty("mail.pop3.host", account.getPop3Server().getName());
        javax.mail.Session session = javax.mail.Session.getInstance(props);

        POP3Store store = null;
        try {
            store = (POP3Store)session.getStore("pop3");
            store.connect(account.getMailAddress().getLocalName(), account.getMailAddress().getPassword());

            javax.mail.Folder folder = store.getFolder("INBOX");
            folder.open(javax.mail.Folder.READ_ONLY);

            javax.mail.Message[] messages = folder.getMessages();
            System.out.println("Message count:"+messages.length);
            for (javax.mail.Message message: messages) {
                System.out.println("Subject:"+message.getSubject());
            }

        } catch (javax.mail.NoSuchProviderException e) {
            e.printStackTrace();
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
    }
}
