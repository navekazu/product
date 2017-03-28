package tools.filer.command;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandHandlerTest {
    @Test
    public void parseCommandTest() throws Exception {
        CommandHandler handler = new CommandHandler();
        List<Command> list;

        list = handler.parseCommand("cd c:\\test1 | fil *.txt *.docx \"hello world.xlsx\" | sel * | cp c:\\test2");
        assertEquals(4, list.size());
    }

    @Test
    public void prepareTest() throws Exception {
        CommandHandler handler = new CommandHandler();
        List<Command> list;

        list = handler.parseCommand("cd c:\\test1 | fil *.txt *.docx \"hello world.xlsx\" | sel * | cp c:\\test2");
        handler.prepare(list);
    }


}
