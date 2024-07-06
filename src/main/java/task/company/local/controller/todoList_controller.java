package task.company.local.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import task.company.local.config.JwtService;
import task.company.local.dto.request.request_todo;
import task.company.local.dto.request.request_update_todo;
import task.company.local.dto.request.update_checkbox;
import task.company.local.dto.response.response_todo;
import task.company.local.entity.CustomUserDetails;
import task.company.local.entity.todoList_entity;
import task.company.local.entity.user_entity;
import task.company.local.service.todoList_service;
import task.company.local.service.user_service;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/todo")
public class todoList_controller {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private user_service user_service;

    @Autowired
    private todoList_service todoList_service;

    @GetMapping("/")
    public ResponseEntity<List<response_todo>> getAllTodoList() {
        List<response_todo> todos = todoList_service.getAllTodoList();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<response_todo> getById(@PathVariable("id") Long id) {
        todoList_entity entity = todoList_service.findByIdTodo(id);

        long daysBetween = todoList_service.calculateDaysBetweenCreatedAndFinish(entity);

        response_todo todoResponse = response_todo.builder()
                .Id(entity.getId())
                .SubTitle(entity.getSubTitle())
                .Descraption(entity.getDescraption())
                .IsComplete(entity.getIsComplete())
                .FinishAt(entity.getFinishAt())
                .CreatedDate(entity.getCreatedDate())
                .daysBetween(daysBetween)
                .User_Id(id)
                .build();

        return ResponseEntity.ok(todoResponse);
    }

    @PostMapping("/")
    public ResponseEntity<?> createTodo(@RequestBody request_todo dto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        Authentication authentication = jwtService.getAuthentication(token);

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        user_entity user = user_service.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        request_todo newTodo = request_todo.builder()
                .SubTitle(dto.getSubTitle())
                .Descraption(dto.getDescraption())
                .IsComplete(false)
                .FinishAt(dto.getFinishAt())
                .User_Id(user.getId())
                .build();

        todoList_entity savedTodo = todoList_service.CreateTodo(newTodo, email);

        if (savedTodo.getUser() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to associate the user with the todo item.");
        }

        long daysBetween = todoList_service.calculateDaysBetweenCreatedAndFinish(savedTodo);

        response_todo todoResponse = response_todo.builder()
                .Id(savedTodo.getId())
                .SubTitle(savedTodo.getSubTitle())
                .Descraption(savedTodo.getDescraption())
                .IsComplete(savedTodo.getIsComplete())
                .CreatedDate(savedTodo.getCreatedDate())
                .FinishAt(savedTodo.getFinishAt())
                .User_Id(savedTodo.getUser().getId())
                .daysBetween(daysBetween)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(todoResponse);
    }

    @PatchMapping("/{Id}")
    public ResponseEntity<?> updateTodo(@PathVariable("Id") Long id,
            @RequestBody request_update_todo newTodoData) {
        try {
            request_update_todo updatedTodo = todoList_service.updateTodoEntity(id, newTodoData);
            return ResponseEntity.ok(updatedTodo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/checkbox/{Id}")
    public ResponseEntity<?> updateIsComplete(@PathVariable("Id") Long id, @RequestBody Map<String, Boolean> request) {
        Boolean isComplete = request.get("isComplete");
        if (isComplete == null) {
            return ResponseEntity.badRequest().body("Invalid request payload");
        }

        try {
            update_checkbox updatedTodo = todoList_service.updateCheckBoxTodo(id, isComplete);
            return ResponseEntity.ok(updatedTodo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found with id: " + id);
        }
    }

    @DeleteMapping("/{Id}")
    public void deleteTodo(@PathVariable("Id") Long Id) {
        todoList_service.deleteTodo(Id);
    }

}
