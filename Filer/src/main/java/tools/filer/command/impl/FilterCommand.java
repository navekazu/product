package tools.filer.command.impl;

import tools.filer.command.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FilterCommand extends CommandBase implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.INTERMEDIATE_OPERATIONS;
    }

    @Override
    public String getCommand() {
        return "fil";
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
        for (String paramPath: commandParameter.getCommandParameterList()) {
//            Path directory;

//            Path path = getPath(commandParameter.getCurrentDirectory(), Paths.get(paramPath));
            try {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(commandParameter.getCurrentDirectory(), paramPath)) {
                    for (Path entry: stream) {
                        FilerPath filerPath = FilerPath.builder()
                                .path(entry)
                                .selected(false)
                                .build();
                        commandParameter.getFilerPathList().add(filerPath);
                    }
                }
            } catch (IOException e) {
                throw new FilerException(e);
            }
        }
    }
}
