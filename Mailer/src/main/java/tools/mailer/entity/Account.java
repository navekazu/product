package tools.mailer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private String accountName;
    private MailAddress mailAddress;
    private Server pop3Server;
    private Server smtpServer;
    private Server imapServer;
}
