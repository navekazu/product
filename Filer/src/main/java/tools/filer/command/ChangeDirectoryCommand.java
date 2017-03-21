package tools.filer.command;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ChangeDirectoryCommand implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.SINGLE_OPERATIONS;
    }

    @Override
    public void checkParameter(String[] parameters) throws FilerException {
        // パラメータは1つだけ
        if (parameters.length!=1) {
            throw new FilerException("パラメータが多すぎます。");
        }
    }

    @Override
    public List<FilerPath> prepare(Path currentDirectory, List<FilerPath> filerPathList, String[] parameters) throws FilerException {
        Path path = Paths.get(currentDirectory.toString(), parameters[0]);
        return null;
    }

    @Override
    public List<FilerPath> execute(Path currentDirectory, List<FilerPath> filerPathList, String[] parameters) throws FilerException {
        return null;
    }
}
