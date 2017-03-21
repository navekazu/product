package tools.filer.command;

import java.nio.file.Path;
import java.util.List;

public interface Command {
    public COMMAND_KIND getKind();
    public String getCommand();
    public void checkParameter(CommandParameter commandParameter) throws FilerException;
    public void prepare(CommandParameter commandParameter) throws FilerException;
    public void execute(CommandParameter commandParameter) throws FilerException;
}
