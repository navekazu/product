package tools.addressprinter.entity;

import lombok.*;
import lombok.experimental.Builder;

@AllArgsConstructor
@lombok.Data
@NoArgsConstructor
@Builder
public class ZipNo {
    public String sectionNo;
    public String cityNo;
}
