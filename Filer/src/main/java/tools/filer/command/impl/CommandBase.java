package tools.filer.command.impl;

import tools.filer.command.Command;
import tools.filer.command.FilerException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class CommandBase implements Command {
    protected Path getPath(Path currentDirectory, Path file) throws FilerException {
        Path path =file.isAbsolute()?
                // 絶対パス
                file:
                // 相対パス
                Paths.get(currentDirectory.toString(), file.toString());

        if (!Files.exists(path)) {
            throw new FilerException("フォルダが見つかりません。");
        }

        return path;
    }
}
