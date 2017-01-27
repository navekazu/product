package tools.mailer.plugin;

import tools.mailer.di.anntation.Plugin;
import tools.mailer.di.anntation.Process;
import tools.mailer.di.anntation.ProcessType;
import tools.mailer.entity.Account;
import tools.mailer.entity.Message;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Plugin
public class PersistencePlugin {

    @Process(processType= ProcessType.SAVE_MAIL)
    public void saveMail(Account account, javax.mail.Message message) {
        try (FileOutputStream out = new FileOutputStream("c:\\message.mail")) {
            InputStream in = message.getInputStream();
            int data;
            while ((data=in.read())!=-1) {
                out.write(data);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("saveMail");

    }

    @Process(processType= ProcessType.LOAD_MAIL)
    public void loadMail(Account account) {

    }
}
