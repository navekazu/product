package tools.filer.command;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ChangeDirectoryCommand implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.SINGLE_OPERATIONS;
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public void checkParameter(CommandParameter commandParameter) throws FilerException {
        // パラメータは1つだけ
        if (commandParameter.getParameterList().size()!=1) {
            throw new FilerException("パラメータが多すぎます。");
        }
    }

    private Path getPath(CommandParameter commandParameter) throws FilerException {
        Path path = Paths.get(commandParameter.getCurrentDirectory().toString(), commandParameter.getParameterList().get(0));

        if (!Files.exists(path)) {
            throw new FilerException("フォルダが見つかりません。");
        }

        return path;
    }

    @Override
    public void prepare(CommandParameter commandParameter) throws FilerException {
        getPath(commandParameter);
    }

    @Override
    public void execute(CommandParameter commandParameter) throws FilerException {
        Path path = getPath(commandParameter);

        commandParameter.setCurrentDirectory(path);
    }
}
