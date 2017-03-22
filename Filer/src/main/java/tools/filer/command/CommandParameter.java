package tools.filer.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.nio.file.Path;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandParameter {
    private Path currentDirectory;
    private List<FilerPath> filerPathList;
    private List<String> parameterList;
}
