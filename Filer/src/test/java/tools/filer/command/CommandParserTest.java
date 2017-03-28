package tools.filer.command;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandParserTest {
    @Test
    public void parseTest() {
        List<CommandParameter> list;

        list = CommandParser.parse("cd c:\\test1 | fil *.txt *.docx \"hello world.xlsx\" | sel * | cp c:\\test2");

        assertEquals(4, list.size());

        assertEquals("cd", list.get(0).getCommand());
        assertEquals(1, list.get(0).getCommandParameterList().size());
        assertEquals("c:\\test1", list.get(0).getCommandParameterList().get(0));
        assertEquals(0, list.get(0).getStartCommandLineIndex());
        assertEquals(12, list.get(0).getEndCommandLineIndex());

        assertEquals("fil", list.get(1).getCommand());
        assertEquals(3, list.get(1).getCommandParameterList().size());
        assertEquals("*.txt", list.get(1).getCommandParameterList().get(0));
        assertEquals("*.docx", list.get(1).getCommandParameterList().get(1));
        assertEquals("hello world.xlsx", list.get(1).getCommandParameterList().get(2));
        assertEquals(13, list.get(1).getStartCommandLineIndex());
        assertEquals(50, list.get(1).getEndCommandLineIndex());

        assertEquals("sel", list.get(2).getCommand());
        assertEquals(1, list.get(2).getCommandParameterList().size());
        assertEquals("*", list.get(2).getCommandParameterList().get(0));

        assertEquals("cp", list.get(3).getCommand());
        assertEquals(1, list.get(3).getCommandParameterList().size());
        assertEquals("c:\\test2", list.get(3).getCommandParameterList().get(0));
    }
}
