package tools.reader.reader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReaderFactoryTest {
    @Test
    public void createReaderTest() throws InstantiationException, IllegalAccessException {
        assertEquals(AtomReader.class.getName(), ReaderFactory.createReader("application/atom+xml").getClass().getName());
        assertEquals(RssReader.class.getName(), ReaderFactory.createReader("application/rss+xml").getClass().getName());
        assertEquals(EmptyReader.class.getName(), ReaderFactory.createReader("some mime").getClass().getName());
    }
}
