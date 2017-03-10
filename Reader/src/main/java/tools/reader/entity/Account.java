package tools.reader.entity;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;

public class Account {
    private String title;
    private URL link;
    private String description;
    private ZonedDateTime lastUpdate;
    private List<Item> itemList;
}
