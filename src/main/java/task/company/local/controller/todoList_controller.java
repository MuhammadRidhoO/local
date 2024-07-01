package task.company.local.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import task.company.local.dto.request.request_todo;
import task.company.local.dto.request.request_update_todo;
import task.company.local.dto.response.response_todo;
import task.company.local.entity.todoList_entity;
import task.company.local.service.todoList_service;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/todo")
public class todoList_controller {
    
    @Autowired
    private todoList_service todoList_service;

    @GetMapping("/")
    public ResponseEntity<List<response_todo>> getAllTodoList() {
      List<response_todo> todos = todoList_service.getAllTodoList();
      return ResponseEntity.ok(todos);
  }
    @GetMapping("/{id}")
    public ResponseEntity<response_todo> getById(@PathVariable("id") Long id) {
      todoList_entity entity = todoList_service.findByIdTodo(id)
          .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));

      long daysBetween = todoList_service.calculateDaysBetweenCreatedAndFinish(entity);

      response_todo todoResponse = response_todo.builder()
          .Id(entity.getId())
          .SubTitle(entity.getSubTitle())
          .Descraption(entity.getDescraption()) // Pastikan penamaan konsisten
          .IsComplete(entity.getIsComplete())
          .FinishAt(entity.getFinishAt())
          .CreatedDate(entity.getCreatedDate())
          .daysBetween(daysBetween)
          .build();

      return ResponseEntity.ok(todoResponse);
  }

    @PostMapping("/")
    public ResponseEntity<?> createTodo(@RequestBody request_todo dto) {

        todoList_entity new_todo = todoList_service.CreateTodo(dto);
        long daysBetween = todoList_service.calculateDaysBetweenCreatedAndFinish(new_todo);
        response_todo todo_response = response_todo.builder()
        .Id(new_todo.getId())
        .SubTitle(new_todo.getSubTitle())
        .Descraption(new_todo.getDescraption())
        .IsComplete(new_todo.getIsComplete())
        .CreatedDate(new_todo.getCreatedDate())
        .FinishAt(new_todo.getFinishAt())
        .daysBetween(daysBetween)
        .build();

        System.out.println(todo_response + " Check Post Todo in Controller");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(todo_response);

  }

   @PatchMapping("/{Id}")
   public todoList_entity updateTodo(@PathVariable("Id") Long id, @RequestBody request_update_todo newTodoData) {
       return todoList_service.updateTodo(id, newTodoData);
   }
    

   @PatchMapping("/checkbox/{Id}")
   public ResponseEntity<?> updateIsComplete(@PathVariable("Id") Long Id, @RequestBody Map<String, Boolean> request) {
    Boolean isComplete = request.get("isComplete");
    if (isComplete == null) {
        return ResponseEntity.badRequest().body("Invalid request payload");
    }

    try {
        todoList_entity updatedTodo = todoList_service.updateCheckBoxTodo(Id, isComplete);
        return ResponseEntity.ok(updatedTodo);
    } catch (EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found with id: " + Id);
    }
}

    @DeleteMapping("/{Id}")
    public void deleteTodo(@PathVariable("Id") Long Id) {
        todoList_service.deleteTodo(Id);
    }

}
