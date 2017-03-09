package tools.reader.reader;

public class EmptyReader extends ReaderBase implements Reader {
    @Override
    public boolean isAcceptableMime(String mime) {
        // accept anything.
        return true;
    }
}
