package tools.reader.reader;

import java.util.*;

public class ReaderFactory {
    private static List<Reader> FACTORY_STORAGE = new ArrayList<>();
    static {
        FACTORY_STORAGE.add(new AtomReader());
        FACTORY_STORAGE.add(new RssReader());
    }
    private static Reader EMPTY_READER = new EmptyReader();

    public static Reader createReader(String mime) throws IllegalAccessException, InstantiationException {
        Optional<Reader> reader = FACTORY_STORAGE.stream()
                .filter(r -> r.isAcceptableMime(mime))
                .findFirst();
        return reader.orElse(EMPTY_READER).getClass().newInstance();
    }
}
