package tools.filer.command;

import java.nio.file.Path;
import java.util.List;

public class MoveCommand implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.TERMINAL_OPERATIONS;
    }

    @Override
    public void checkParameter(String[] parameters) throws FilerException {

    }

    @Override
    public List<FilerPath> prepare(Path currentDirectory, List<FilerPath> filerPathList, String[] parameters) throws FilerException {
        return null;
    }

    @Override
    public List<FilerPath> execute(Path currentDirectory, List<FilerPath> filerPathList, String[] parameters) throws FilerException {
        return null;
    }
}
