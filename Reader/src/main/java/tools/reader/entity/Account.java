package tools.reader.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.io.Serializable;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class Account implements Serializable {
    private String title;
    private URL link;
    private String description;
    private ZonedDateTime lastUpdate;
    private List<Item> itemList;
}
