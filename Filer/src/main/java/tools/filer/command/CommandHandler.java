package tools.filer.command;

import tools.filer.command.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler {
    private List<Command> commandInstanceList;

    public CommandHandler() {
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

    List<Command> parseCommand(String command) throws FilerException {
        List<CommandParameter> commandParameterList = CommandParser.parse(command);
        List<Command> commandList = new ArrayList<>();

        for (CommandParameter param: commandParameterList) {
            Command commandInstance = getCommandInstance(param.getCommand());
            if (commandInstance==null) {
                throw new FilerException();
            }
            commandList.add(commandInstance);
        }

        return commandList;
    }

    private Command getCommandInstance(String command) {
        for (Command commandInstance: commandInstanceList) {
            if (command.equals(commandInstance.getCommand())) {
                return commandInstance;
            }
        }
        return null;
    }

    void prepare(List<Command> commandList) {
        for (Command command: commandList) {
//            command.prepare();
        }
    }
}
