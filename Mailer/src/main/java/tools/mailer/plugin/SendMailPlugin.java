package tools.mailer.plugin;

import tools.mailer.di.anntation.*;
import tools.mailer.di.anntation.Process;
import tools.mailer.processor.MailProcessor;

@Plugin
public class SendMailPlugin {
    @Autowired
    public MailProcessor mailProcessor;

    @Process(processType = ProcessType.SEND_MAIL)
    public void sendMail() {

    }
}
