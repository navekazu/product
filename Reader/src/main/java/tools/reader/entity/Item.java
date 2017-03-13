package tools.reader.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.io.Serializable;
import java.net.URL;

@Data
@NoArgsConstructor
@Builder
public class Item implements Serializable {
    private String title;
    private URL link;
    private String description;
    private URL thumbnail;
}
