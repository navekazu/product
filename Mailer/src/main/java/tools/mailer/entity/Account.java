package tools.mailer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
public class Account {
    private String accountName;
    private MailAddress mailAddress;
    private Server pop3Server;
    private Server smtpServer;
    private Server imapServer;
}
