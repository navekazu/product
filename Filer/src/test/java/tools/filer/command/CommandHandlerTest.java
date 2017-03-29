package tools.filer.command;

import org.junit.Test;
import tools.filer.FilerInterface;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandHandlerTest {
    @Test
    public void parseCommandTest() throws Exception {
        FilerInterfaceMock filerInterface = new FilerInterfaceMock();
        CommandHandler handler = new CommandHandler(filerInterface);
        List<CommandParameter> list;

        list = handler.parseCommand("cd c:\\test1 | fil *.txt *.docx \"hello world.xlsx\" | sel * | cp c:\\test2");
        assertEquals(4, list.size());
    }

    @Test
    public void prepareTest() throws Exception {
        FilerInterfaceMock filerInterface = new FilerInterfaceMock();
        CommandHandler handler = new CommandHandler(filerInterface);
        List<CommandParameter> list;

        list = handler.parseCommand("cd c:\\temp | fil *.txt *.docx \"hello world.xlsx\" | sel .* | cp c:\\test2");
        handler.prepare(list);
    }

    private static class FilerInterfaceMock implements FilerInterface {
        Path currentDirectory = Paths.get("C:\\temp");

        @Override
        public Path getCurrentDirectory() {
            return currentDirectory;
        }

        @Override
        public void setCurrentDirectory(Path path) {
            currentDirectory = path;
        }
    }

}
