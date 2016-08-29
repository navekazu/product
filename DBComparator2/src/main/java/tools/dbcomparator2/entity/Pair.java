package tools.dbcomparator2.entity;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class Pair<F, S> {
    private F first;
    private S second;
}
