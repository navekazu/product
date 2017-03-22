package tools.filer.command;

import java.nio.file.Path;
import java.util.List;

public class MoveCommand implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.TERMINAL_OPERATIONS;
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
