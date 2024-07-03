package task.company.local.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import task.company.local.entity.todoList_entity;
import task.company.local.entity.user_entity;

@Repository
public interface todoList_repository extends JpaRepository<todoList_entity, Long> {
    List<todoList_entity> findByUser(user_entity user);
}
