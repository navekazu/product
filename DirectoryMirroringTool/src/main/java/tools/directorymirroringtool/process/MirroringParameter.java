package tools.directorymirroringtool.process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.nio.file.Path;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MirroringParameter {
    String name;
    Path sourcePath;
    Path sinkPath;
    MirroringStatus status;
    int syncInterval;
    Date lastBackupDate;
    Date nextBackupDate;
}
