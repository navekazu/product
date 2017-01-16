package tools.addressprinter.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@AllArgsConstructor
@lombok.Data
@NoArgsConstructor
@Builder
public class Data {
    public Font font;
    public List<Address> addressList;
}
