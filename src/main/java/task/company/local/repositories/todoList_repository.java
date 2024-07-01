package task.company.local.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import task.company.local.entity.todoList_entity;

@Repository
public interface  todoList_repository extends JpaRepository<todoList_entity, Long> {
    Optional<todoList_entity> findById(Long Id);
}
