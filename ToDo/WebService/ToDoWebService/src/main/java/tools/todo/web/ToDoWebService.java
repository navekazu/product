package tools.todo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.todo.entity.ToDo;

@RestController
public class ToDoWebService {
    @RequestMapping("/todo")
    public ToDo getToDo(@RequestParam(value="id") Long id) {
        return null;
    }

}
