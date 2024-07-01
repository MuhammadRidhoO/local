package task.company.local.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import task.company.local.dto.request.request_todo;
import task.company.local.dto.response.response_todo;
import task.company.local.entity.todoList_entity;
import task.company.local.service.todoList_service;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private todoList_service todolist_service;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/todo")
    public String getAllTodos(Model model) {
        List<response_todo> todos = todolist_service.getAllTodoList();
        model.addAttribute("todos", todos);
        return "task";
    }

    // @GetMapping("/todo/{id}")
    // public String getItemById(@PathVariable("id") Long id, Model model) {
    //     Optional<todoList_entity> todoById = todolist_service.findByIdTodo(id);
    //     if (todoById.isPresent()) {
    //         model.addAttribute("todo", todoById.get());
    //     } else {
    //         model.addAttribute("error", "Todo not found with id: " + id);
    //     }
    //     return "index";
    // }
    
    
    @GetMapping("/todo/{id}")
    public String getItemById(@PathVariable("id") Long id, Model model) {
 
        String url = "http://localhost:8080/api/v1/todo/" + todolist_service.findByIdTodo(id);
        todoList_entity todoList = restTemplate.getForObject(url, todoList_entity.class);
        model.addAttribute("todo", todoList);
        return "task";
    }

    @PostMapping("/")
    public String createTask(@ModelAttribute request_todo dto, Model model) {
        // Simpan task ke database
        todoList_entity new_todo = todolist_service.CreateTodo(dto);
        model.addAttribute("todo", new_todo);
        return "task";
    }
}
