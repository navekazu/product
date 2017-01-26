package tools.mailer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private MailAddress from;
    private MailAddress[] to;
    private MailAddress[] cc;
    private MailAddress[] bcc;
    private Date sentDate;
    private String subject;
    private String text;
}
