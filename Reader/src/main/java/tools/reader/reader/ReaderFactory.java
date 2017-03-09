package tools.reader.reader;

import java.util.HashMap;
import java.util.Map;

public class ReaderFactory {
    private static Map<String, Class> FACTORY_STORAGE = new HashMap<>();
    static {
        FACTORY_STORAGE.put(AtomReader.getAcceptableMime(), AtomReader.class);
        FACTORY_STORAGE.put(RssReader.getAcceptableMime(), RssReader.class);
    }

    public static Reader createReader(String mime) throws IllegalAccessException, InstantiationException {
        if (!FACTORY_STORAGE.containsKey(mime)) {
            throw new IllegalArgumentException();
        }
        return (Reader)FACTORY_STORAGE.get(mime).newInstance();
    }
}
