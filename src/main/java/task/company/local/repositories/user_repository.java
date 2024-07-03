package task.company.local.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import task.company.local.entity.user_entity;

@Repository
public interface user_repository extends JpaRepository<user_entity, Long> {

    boolean existsByEmail(String email);

    Optional<user_entity> findByEmail(String email);

    @Query("SELECT u FROM user_entity u LEFT JOIN FETCH u.todoListEntities WHERE u.email = :email")
    Optional<user_entity> findByEmailWithTodos(@Param("email") String email);
}
