package tools.filer.command.impl;

import tools.filer.command.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeDirectoryCommand extends CommandBase implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.SINGLE_OPERATIONS;
    }

    @Override
    public String getCommand() {
        return "cd";
    }

    @Override
    public void checkParameter(CommandParameter commandParameter) throws FilerException {
        // パラメータは1つだけ
        if (commandParameter.getCommandParameterList().size()!=1) {
            throw new FilerException("パラメータが多すぎます。");
        }
    }

    @Override
    public void prepare(CommandParameter commandParameter) throws FilerException {
        getPath(commandParameter.getCurrentDirectory(), Paths.get(commandParameter.getCommandParameterList().get(0)));
    }

    @Override
    public void execute(CommandParameter commandParameter) throws FilerException {
        Path path = getPath(commandParameter.getCurrentDirectory(), Paths.get(commandParameter.getCommandParameterList().get(0)));
        commandParameter.setCurrentDirectory(path);
    }
}
