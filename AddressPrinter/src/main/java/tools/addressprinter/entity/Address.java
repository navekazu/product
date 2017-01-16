package tools.addressprinter.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.List;

@AllArgsConstructor
@lombok.Data
@NoArgsConstructor
@Builder
public class Address {
    public ZipNo zipNo;
    public String address1;
    public String address2;
    public String address3;
    public List<Person> personList;
    public boolean useFamilyNameForEveryone;
}
