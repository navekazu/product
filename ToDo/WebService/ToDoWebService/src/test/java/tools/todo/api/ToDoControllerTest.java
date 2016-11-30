package tools.todo.api;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToDoControllerTest {
    @Autowired
    private ToDoController toDoController;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    /** 事前処理 */
    @Before
    public void 事前処理() throws Exception {
        mockMvc = webAppContextSetup(wac).build();
    }

    /** 事後処理 */
    @After
    public void 事後処理() throws Exception {
    }
}
