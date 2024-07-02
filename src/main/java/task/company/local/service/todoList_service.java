package task.company.local.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import task.company.local.dto.request.request_todo;
import task.company.local.dto.request.request_update_todo;
import task.company.local.dto.response.response_todo;
import task.company.local.entity.todoList_entity;
import task.company.local.entity.user_entity;
import task.company.local.repositories.todoList_repository;

@Service
public class todoList_service {

  @Autowired
  private todoList_repository todolist_repository;

  @Autowired
  private user_service userService;

  public List<response_todo> getAllTodoList() {
    List<todoList_entity> todoList = todolist_repository.findAll();
    return todoList.stream().map(todo -> {
      long daysBetween = calculateDaysBetweenCreatedAndFinish(todo);
      return response_todo.builder()
          .Id(todo.getId())
          .SubTitle(todo.getSubTitle())
          .Descraption(todo.getDescraption())
          .IsComplete(todo.getIsComplete())
          .FinishAt(todo.getFinishAt())
          .CreatedDate(todo.getCreatedDate())
          // .user_entity_id(todo.getUser_entity().getId())
          .daysBetween(daysBetween)
          .build();
    }).toList();
  }

  public Optional<todoList_entity> findByIdTodo(Long id) {
    return todolist_repository.findById(id);
  }

  public todoList_entity CreateTodo(request_todo dto, String userEmail) {

    user_entity user = userService.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));

    todoList_entity new_todoList_entity = new todoList_entity();
    new_todoList_entity.setSubTitle(dto.getSubTitle());
    new_todoList_entity.setDescraption(dto.getDescraption());
    new_todoList_entity.setIsComplete(false);
    new_todoList_entity.setFinishAt(dto.getFinishAt());
    new_todoList_entity.setUser_entity(user);

    todolist_repository.save(new_todoList_entity);

    System.out.println(new_todoList_entity + " Check Post Todo in Service");

    return new_todoList_entity;

  }

  public long calculateDaysBetween(LocalDateTime finishAt) {
    LocalDateTime now = LocalDateTime.now();
    return ChronoUnit.DAYS.between(now, finishAt);
  }

  public todoList_entity updateTodo(Long Id, request_update_todo updateTodo) {
    return todolist_repository.findById(Id).map(todo -> {
      todo.setDescraption(updateTodo.getDescraption());
      todo.setFinishAt(updateTodo.getFinishAt());
      todo.setIsComplete(updateTodo.getIsComplete());
      todo.setSubTitle(updateTodo.getSubTitle());
      return todolist_repository.save(todo);
    }).orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + Id));
  }

  public todoList_entity updateCheckBoxTodo(Long Id, Boolean IsComplete) {
    todoList_entity todo = todolist_repository.findById(Id)
        .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + Id));
    todo.setIsComplete(IsComplete);
    todolist_repository.save(todo);
    return todo;
  }

  public void deleteTodo(Long Id) {
    if (todolist_repository.existsById(Id)) {
      todolist_repository.deleteById(Id);
    } else {
      throw new EntityNotFoundException("Todo not found with id: " + Id);
    }
  }

  public long calculateDaysBetweenCreatedAndFinish(todoList_entity todo) {
    if (todo.getCreatedDate() != null && todo.getFinishAt() != null) {
      return ChronoUnit.DAYS.between(todo.getCreatedDate().toLocalDate(), todo.getFinishAt());
    }
    return 0;
  }

}
