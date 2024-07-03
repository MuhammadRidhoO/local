package task.company.local.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import task.company.local.dto.request.register_request;
import task.company.local.dto.response.UserAlreadyExistException;
import task.company.local.dto.response.response_todo;
import task.company.local.dto.response.user_response;
import task.company.local.entity.todoList_entity;
import task.company.local.entity.user_entity;
import task.company.local.repositories.user_repository;
import task.company.local.security.Bcrypt;

@Service
public class user_service {

    @Autowired
    private user_repository user_repository;

    // public user_service(
    // user_repository user_repository) {
    // this.user_repository = user_repository;
    // }

    @Transactional(readOnly = true)
    public List<user_response> getAllUsers() {
        List<user_entity> users = user_repository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private user_response mapToUserResponse(user_entity user) {
        List<response_todo> todoList = user.getTodoListEntities().stream()
                .map(this::mapToTodoListDTO)
                .collect(Collectors.toList());
        return user_response.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .todoList(todoList)
                .build();
    }

    private response_todo mapToTodoListDTO(todoList_entity todo) {
        long daysBetween = calculateDaysBetweenCreatedAndFinish(todo);
        return response_todo.builder()
                .Id(todo.getId())
                .SubTitle(todo.getSubTitle())
                .Descraption(todo.getDescraption())
                .IsComplete(todo.getIsComplete())
                .FinishAt(todo.getFinishAt())
                .CreatedDate(todo.getCreatedDate())
                .daysBetween(daysBetween)
                .User_Id(todo.getUser().getId())
                .build();
    }

    public long calculateDaysBetweenCreatedAndFinish(todoList_entity todo) {
        if (todo.getCreatedDate() != null && todo.getFinishAt() != null) {
            return ChronoUnit.DAYS.between(todo.getCreatedDate().toLocalDate(), todo.getFinishAt());
        }
        return 0;
    }

    @Transactional(readOnly = true)
    public Optional<user_entity> findByEmail(String email) {
        return user_repository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<user_entity> findById(Long id) {
        return user_repository.findById(id);
    }

    public user_entity RegisterUser(register_request request) throws UserAlreadyExistException {
        if (user_repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException("An account for that email already exists.");
        }

        user_entity newUser = new user_entity();
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(Bcrypt.hashpw(request.getPassword(), Bcrypt.gensalt()));

        return user_repository.save(newUser);
    }

}
