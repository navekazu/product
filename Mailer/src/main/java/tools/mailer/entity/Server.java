package tools.mailer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Server {
    private String name;
    private int port;
    private boolean use;
}
