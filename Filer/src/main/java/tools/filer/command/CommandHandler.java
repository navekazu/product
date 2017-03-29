package tools.filer.command;

import tools.filer.FilerInterface;
import tools.filer.command.impl.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler {
    private FilerInterface filerInterface;
    private List<Command> commandInstanceList;

    public CommandHandler(FilerInterface filerInterface) {
        this.filerInterface = filerInterface;
        initCommandInstanceList();
    }

    private void initCommandInstanceList() {
        commandInstanceList = new ArrayList<>();

        // 中間処理
        commandInstanceList.add(new FilterCommand());
        commandInstanceList.add(new SelectCommand());

        // 終端処理
        commandInstanceList.add(new CopyCommand());
        commandInstanceList.add(new DeleteCommand());
        commandInstanceList.add(new MoveCommand());

        // 単独処理
        commandInstanceList.add(new ChangeDirectoryCommand());
    }

    List<CommandParameter> parseCommand(String command) throws FilerException {
        List<CommandParameter> commandParameterList = CommandParser.parse(command);

        for (CommandParameter param: commandParameterList) {
            Command commandInstance = getCommandInstance(param.getCommand());
            if (commandInstance==null) {
                throw new FilerException();
            }
            param.setCommandInstance(commandInstance);
        }

        return commandParameterList;
    }

    private Command getCommandInstance(String command) {
        for (Command commandInstance: commandInstanceList) {
            if (command.equals(commandInstance.getCommand())) {
                return commandInstance;
            }
        }
        return null;
    }

    void prepare(List<CommandParameter> CommandParameterList) throws FilerException {
        Path currentDirectory = filerInterface.getCurrentDirectory();

        for (CommandParameter param: CommandParameterList) {
            param.setCurrentDirectory(currentDirectory);
            param.getCommandInstance().checkParameter(param);
            param.getCommandInstance().prepare(param);
            currentDirectory = param.getCurrentDirectory();
//            filerInterface.setCurrentDirectory(currentDirectory);
        }
    }
}
