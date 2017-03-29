package tools.filer.command.impl;

import tools.filer.command.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectCommand extends CommandBase implements Command {
    @Override
    public COMMAND_KIND getKind() {
        return COMMAND_KIND.INTERMEDIATE_OPERATIONS;
    }

    @Override
    public String getCommand() {
        return "sel";
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
            Pattern p = Pattern.compile(paramPath);
            for (FilerPath filerPath : commandParameter.getFilerPathList()) {
                Matcher m = p.matcher(filerPath.getPath().getFileName().toString());
                if (m.matches()) {
                    filerPath.setSelected(true);
                }
            }
        }
    }
}
