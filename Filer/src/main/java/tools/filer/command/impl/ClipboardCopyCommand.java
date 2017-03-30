package tools.filer.command.impl;

import tools.filer.command.*;

public class ClipboardCopyCommand extends CommandBase implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return null;
    }

    @Override
    public String getCommand() {
        return "ccp";
    }

    @Override
    public void checkParameter(CommandParameter commandParameter) throws FilerException {

    }

    @Override
    public void prepare(CommandParameter commandParameter) throws FilerException {

    }

    @Override
    public void execute(CommandParameter commandParameter) throws FilerException {

    }
}
