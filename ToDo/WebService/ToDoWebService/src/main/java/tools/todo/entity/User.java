package tools.todo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
public class User implements Owner {
    private String name;
}