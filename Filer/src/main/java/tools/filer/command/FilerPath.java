package tools.filer.command;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.nio.file.Path;

@Data
@NoArgsConstructor
@Builder
public class FilerPath {
    private Path path;
    private boolean selected;
}
