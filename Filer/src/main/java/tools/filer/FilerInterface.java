package tools.filer;

import java.nio.file.Path;
import java.util.List;

public interface FilerInterface {
    public Path getCurrentDirectory();
    public void setCurrentDirectory(Path path);
    public void selectFile(List<Path> list);
    public void filterFile(List<Path> list);
}
