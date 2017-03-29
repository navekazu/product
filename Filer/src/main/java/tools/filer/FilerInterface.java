package tools.filer;

import java.nio.file.Path;

public interface FilerInterface {
    public Path getCurrentDirectory();
    public void setCurrentDirectory(Path path);
}
