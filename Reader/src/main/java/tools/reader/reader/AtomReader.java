package tools.reader.reader;

public class AtomReader extends ReaderBase implements Reader {
    @Override
    public boolean isAcceptableMime(String mime) {
        return "application/atom+xml".equals(mime);
    }
}
