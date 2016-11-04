package tools.todo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.todo.entity.ToDo;
import tools.todo.service.ToDoService;

import java.util.List;
import java.util.Optional;

@RequestMapping("api/todo")
@RestController
public class ToDoController {
    @Autowired
    private ToDoService toDoService;

    @RequestMapping(method= RequestMethod.GET)
    public List<ToDo> getToDoList() {
        return toDoService.getToDoList();
    }

    @RequestMapping(method= RequestMethod.GET, value="{id}")
    public ToDo getToDo(@PathVariable Long id) {
        Optional<ToDo> toDo = toDoService.getToDo(id);
        return toDo.orElse(ToDo.builder()
                .build());
    }

}
