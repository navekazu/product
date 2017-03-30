package tools.filer.command.impl;

import tools.filer.command.*;

public class CopyCommand extends CommandBase implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.TERMINAL_OPERATIONS;
    }

    @Override
    public String getCommand() {
        return "cp";
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
