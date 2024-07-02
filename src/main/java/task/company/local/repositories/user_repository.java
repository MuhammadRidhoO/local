package task.company.local.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import task.company.local.entity.user_entity;

@Repository
public interface user_repository extends JpaRepository<user_entity, Long> {

    boolean existsByEmail(String email);

    Optional<user_entity> findByEmail(String email);
}
