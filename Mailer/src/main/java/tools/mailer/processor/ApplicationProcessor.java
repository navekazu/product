package tools.mailer.processor;

import tools.mailer.di.anntation.Plugin;
import tools.mailer.di.anntation.Process;
import tools.mailer.di.anntation.ProcessType;

@Plugin
public class ApplicationProcessor {

    @Process(processType= ProcessType.BOOT_APPLICATION)
    private void boot() {

    }

    @Process(processType= ProcessType.TERM_APPLICATION)
    private void term() {

    }
}
