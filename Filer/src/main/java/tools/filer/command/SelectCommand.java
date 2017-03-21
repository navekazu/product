package tools.filer.command;

import java.nio.file.Path;
import java.util.List;

public class SelectCommand implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.INTERMEDIATE_OPERATIONS;
    }

    @Override
    public void checkParameter(CommandParameter commandParameter) throws FilerException {

    }

    @Override
    public void prepare(CommandParameter commandParameter) throws FilerException {
        execute(commandParameter);
    }

    @Override
    public void execute(CommandParameter commandParameter) throws FilerException {

    }
}
