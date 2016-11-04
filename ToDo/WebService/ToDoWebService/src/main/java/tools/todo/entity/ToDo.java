package tools.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {
    private long id;
    private String title;
    private ToDoGroup toDoGroup;
    private User user;
}
