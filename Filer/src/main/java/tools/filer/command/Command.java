package tools.filer.command;

import java.nio.file.Path;
import java.util.List;

public interface Command {
    public COMMAND_KIND getKind();
    public void checkParameter(String[] parameters) throws FilerException;
    public List<FilerPath> prepare(Path currentDirectory, List<FilerPath> filerPathList, String[] parameters) throws FilerException;
    public List<FilerPath> execute(Path currentDirectory, List<FilerPath> filerPathList, String[] parameters) throws FilerException;
}
