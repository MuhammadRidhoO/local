package task.company.local.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import task.company.local.entity.todoList_entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class register_response {

  private Long id;

  private String email;

  private String password;

  private String fullName;

  private List<todoList_entity> todoList_entity;

}
