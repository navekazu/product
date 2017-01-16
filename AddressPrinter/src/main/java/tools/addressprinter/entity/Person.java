package tools.addressprinter.entity;

import lombok.*;
import lombok.experimental.Builder;

@AllArgsConstructor
@lombok.Data
@NoArgsConstructor
@Builder
public class Person {
    public String familyName;
    public String name;
}
