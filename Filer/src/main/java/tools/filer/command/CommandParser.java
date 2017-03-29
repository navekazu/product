package tools.filer.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParser {
    private static final char QUOTE = '"';
    private static final char QUOTE_ESCAPE = '\\';
    private static final char PIPE = '|';

    public static List<CommandParameter> parse(String commandLine) {
        List<CommandParameter> list = new ArrayList<>();
        int start = 0;
        boolean quote = false;

        if (commandLine.length()==0) {
            return list;
        }

        for (int i=0; i<commandLine.length(); i++) {
            if (quote) {
                // いまダブルクオート内
                if (commandLine.charAt(i)==QUOTE && commandLine.charAt(i-1)!=QUOTE_ESCAPE) {  // \でエスケープしてる?
                    quote = false;
                }
                continue;
            }

            // いまダブルクオート外

            // ダブルクオート?
            if (commandLine.charAt(i)==QUOTE) {
                quote = true;
                continue;
            }

            // そうでなければコマンド内

            // パイプ?
            if (commandLine.charAt(i)!=PIPE) {
                continue;
            }

            // パイプなら、そこまでをコマンドとして登録
            String aCommand = commandLine.substring(start, i).trim();
            CommandParameter param = CommandParameter.builder()
                    .command(getCommand(aCommand))
                    .commandParameterList(getCommandParameterList(aCommand))
                    .startCommandLineIndex(start)
                    .endCommandLineIndex(i)
                    .filerPathList(new ArrayList<>())
                    .build();
            list.add(param);
            start = i+1;
        }

        String aCommand = commandLine.substring(start).trim();
        CommandParameter param = CommandParameter.builder()
                .command(getCommand(aCommand))
                .commandParameterList(getCommandParameterList(aCommand))
                .build();
        list.add(param);

        return list;
    }

    static String getCommand(String aCommand) {
        // 小文字に
        aCommand = aCommand.toLowerCase();

        // スペースがあれば、その手前までをコマンドとする
        String[] spaces = new String[]{" ", "　", "\t"};
        for (String space: spaces) {
            int index = aCommand.indexOf(space);
            if (index!=-1) {
                return aCommand.substring(0, index);
            }
        }

        // スペースが無ければそのまま
        return aCommand;
    }

    static List<String>  getCommandParameterList(String aCommand) {
        List<String> list = new ArrayList<>();

        if (aCommand.length()==0) {
            return list;
        }

        String cmd = getCommand(aCommand);
        String allParam = aCommand.substring(cmd.length()).trim();
        int start = 0;
        boolean quote = false;

        for (int i=0; i<allParam.length(); i++) {
            if (quote) {
                // いまダブルクオート内
                if (allParam.charAt(i) == QUOTE && allParam.charAt(i - 1) != QUOTE_ESCAPE) {  // \でエスケープしてる?
                    quote = false;
                }
                continue;
            }

            // いまダブルクオート外

            // ダブルクオート?
            if (allParam.charAt(i) == QUOTE) {
                quote = true;
                continue;
            }

            // そうでなければコマンド内

            // スペース?
            if (!isSpace(allParam.charAt(i))) {
                continue;
            }

            String aParam = allParam.substring(start, i).trim();
            list.add(aParam);
            start = i+1;
        }

        String aParam = allParam.substring(start).trim();
        list.add(aParam);

        // ダブルクオートで囲まれたパラメータは、ダブルクオートを削除
        for (int i=0; i<list.size(); i++) {
            String param = list.get(i);
            if (param.startsWith("\"") && param.endsWith("\"")) {
                list.set(i, param.substring(1, param.length()-1));
            }
        }

        return list;
    }

    static boolean isSpace(char c) {
        switch (c) {
            case ' ':
            case '　':
            case '\t':
                return true;
        }
        return false;
    }

}
