package tools.filer.command.impl;

import tools.filer.command.COMMAND_KIND;
import tools.filer.command.Command;
import tools.filer.command.CommandParameter;
import tools.filer.command.FilerException;

public class ClipboardPasteCommand implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return null;
    }

    @Override
    public String getCommand() {
        return null;
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
