package tools.reader.reader;

public class RssReader extends ReaderBase implements Reader {
    @Override
    public boolean isAcceptableMime(String mime) {
        return "application/rss+xml".equals(mime);
    }
}
