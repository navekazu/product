package tools.addressprinter.entity;

import lombok.*;
import lombok.experimental.Builder;

@AllArgsConstructor
@lombok.Data
@NoArgsConstructor
@Builder
public class Font {
    public String name = "ＭＳ Ｐ明朝";
    public double size = 12.0;
    public boolean bold;
    public boolean italic;
    public boolean underline;
}
