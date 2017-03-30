package tools.filer.command.impl;

import tools.filer.command.*;

public class ClipboardPasteCommand extends CommandBase implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return null;
    }

    @Override
    public String getCommand() {
        return "pas";
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
