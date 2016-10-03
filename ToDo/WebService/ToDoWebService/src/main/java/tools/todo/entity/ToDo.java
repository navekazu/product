package tools.todo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
public class ToDo {
    private String title;
    private Group group;
}
