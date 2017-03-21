package tools.filer.command;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommandHandlerTest {
    @Test
    void parseCommandTest() throws Exception {
        CommandHandler handler = new CommandHandler();
        List<Command> list;

        list = handler.parseCommand("cd");
        assertEquals(1, list.size());
    }
}
