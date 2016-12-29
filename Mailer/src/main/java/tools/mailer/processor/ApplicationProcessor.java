package tools.mailer.processor;

import tools.mailer.di.anntation.Plugin;
import tools.mailer.di.anntation.Process;
import tools.mailer.di.anntation.ProcessType;

@Plugin
public class ApplicationProcessor {

    @Process(processType= ProcessType.BOOT_APPLICATION)
    public void boot() {
        System.out.println("boot");
    }

    @Process(processType= ProcessType.TERM_APPLICATION)
    public void term() {
        System.out.println("term");
    }
}
