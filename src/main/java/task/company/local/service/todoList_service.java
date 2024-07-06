package task.company.local.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import task.company.local.dto.request.request_todo;
import task.company.local.dto.request.request_update_todo;
import task.company.local.dto.request.update_checkbox;
import task.company.local.dto.response.response_todo;
import task.company.local.entity.todoList_entity;
import task.company.local.entity.user_entity;
import task.company.local.repositories.todoList_repository;

@Service
public class todoList_service {

  @Autowired
  private todoList_repository todolist_repository;

  @Autowired
  private user_service user_service;

  public List<response_todo> getAllTodoList() {
    List<todoList_entity> todoList = todolist_repository.findAll();
    return todoList.stream().map(todo -> {
      long daysBetween = calculateDaysBetweenCreatedAndFinish(todo);
      // System.out.println(todo.getUser().getId()+" check User_Id dalam findAll");
      return response_todo.builder()
          .Id(todo.getId())
          .SubTitle(todo.getSubTitle())
          .Descraption(todo.getDescraption())
          .IsComplete(todo.getIsComplete())
          .FinishAt(todo.getFinishAt())
          .CreatedDate(todo.getCreatedDate())
          .User_Id(todo.getUser().getId())
          .daysBetween(daysBetween)
          .build();
    }).toList();
  }

  public todoList_entity findByIdTodo(Long Id) {
    System.out.println(Id + " Id dalam service");
    return todolist_repository.findById(Id)
        .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + Id));
  }

  public todoList_entity CreateTodo(request_todo dto, String userEmail) {

    user_entity user = user_service.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("User not found"));

    todoList_entity new_todoList_entity = new todoList_entity();
    new_todoList_entity.setSubTitle(dto.getSubTitle());
    new_todoList_entity.setDescraption(dto.getDescraption());
    new_todoList_entity.setIsComplete(false);
    new_todoList_entity.setFinishAt(dto.getFinishAt());
    new_todoList_entity.setUser(user);

    todolist_repository.save(new_todoList_entity);

    System.out.println(new_todoList_entity + " Check Post Todo in Service");

    return new_todoList_entity;

  }

  public long calculateDaysBetween(LocalDateTime finishAt) {
    LocalDateTime now = LocalDateTime.now();
    return ChronoUnit.DAYS.between(now, finishAt);
  }

  @Transactional
  public request_update_todo updateTodoEntity(Long id, request_update_todo updateTodo) {
    return todolist_repository.findById(id).map(todo -> {
      todo.setDescraption(updateTodo.getDescraption());
      todo.setFinishAt(updateTodo.getFinishAt());
      todo.setIsComplete(updateTodo.getIsComplete());
      todo.setSubTitle(updateTodo.getSubTitle());
      todoList_entity updatedTodo = todolist_repository.save(todo);

      // Map updatedTodo to DTO
      request_update_todo todoListDTO = new request_update_todo();
      todoListDTO.setId(updatedTodo.getId());
      todoListDTO.setDescraption(updatedTodo.getDescraption());
      todoListDTO.setFinishAt(updatedTodo.getFinishAt());
      todoListDTO.setIsComplete(updatedTodo.getIsComplete());
      todoListDTO.setSubTitle(updatedTodo.getSubTitle());
      return todoListDTO;
    }).orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
  }

  public todoList_entity updateCheckBoxTodoEntity(Long id, Boolean isComplete) {
    todoList_entity todo = todolist_repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
    todo.setIsComplete(isComplete);
    return todolist_repository.save(todo);
  }

  public update_checkbox updateCheckBoxTodo(Long id, Boolean isComplete) {
    todoList_entity updatedTodo = updateCheckBoxTodoEntity(id, isComplete);
    return new update_checkbox(updatedTodo.getIsComplete());
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
