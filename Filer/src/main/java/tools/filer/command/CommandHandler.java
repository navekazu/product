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
        String[] commands = command.split("|");

        List<Command> list = new ArrayList<>();

        for (String cmd: commands) {
            Command commandInstance = getCommandInstance(cmd);
            if (commandInstance==null) {
                throw new FilerException();
            }
            list.add(commandInstance);
        }

        return list;
    }

    private Command getCommandInstance(String command) {
        for (Command commandInstance: commandInstanceList) {
            if (command.equals(commandInstance.getCommand())) {
                return commandInstance;
            }
        }
        return null;
    }

    String getCommand(String command) {
        // 小文字に
        command = command.toLowerCase();

        // スペースがあれば、その手前までをコマンドとする
        String[] spaces = new String[]{" ", "　", "\t"};
        for (String space: spaces) {
            int index = command.indexOf(space);
            if (index!=-1) {
                return command.substring(0, index);
            }
        }

        // スペースが無ければそのまま
        return command;
    }

    void prepare(String command) {

    }
}
