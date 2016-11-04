package tools.todo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.todo.entity.ToDo;
import tools.todo.entity.ToDoGroup;
import tools.todo.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ToDoService {
    private List<ToDo> mockData;
    public ToDoService() {
        mockData = new ArrayList<>();
        mockData.add(ToDo.builder()
                .id(0)
                .title("test01")
                .toDoGroup(ToDoGroup.builder()
                        .id(0)
                        .name("group")
                        .build())
                .build());
        mockData.add(ToDo.builder()
                .id(1)
                .title("test02")
                .toDoGroup(ToDoGroup.builder()
                        .id(0)
                        .name("group")
                        .build())
                .build());
        mockData.add(ToDo.builder()
                .id(2)
                .title("test03")
                .toDoGroup(ToDoGroup.builder()
                        .id(0)
                        .name("group")
                        .build())
                .build());
        mockData.add(ToDo.builder()
                .id(3)
                .title("test04")
                .toDoGroup(ToDoGroup.builder()
                        .id(0)
                        .name("group")
                        .build())
                .build());
        mockData.add(ToDo.builder()
                .id(4)
                .title("test05")
                .toDoGroup(ToDoGroup.builder()
                        .id(0)
                        .name("group")
                        .build())
                .build());
    }

    public List<ToDo> getToDoList() {
        return mockData;
    }

    public Optional<ToDo> getToDo(long id) {
        return mockData.stream().filter(entity -> entity.getId()==id).findFirst();
    }
}
