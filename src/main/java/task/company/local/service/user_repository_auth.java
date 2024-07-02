package task.company.local.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import task.company.local.entity.user_entity;


@Repository
public interface user_repository_auth extends CrudRepository<user_entity, Long> {
  public user_entity findByEmail(String email);
}