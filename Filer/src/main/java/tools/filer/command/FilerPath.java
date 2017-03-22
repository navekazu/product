package tools.filer.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilerPath {
    private Path path;
    private boolean selected;
}
