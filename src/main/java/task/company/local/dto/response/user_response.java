package task.company.local.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class user_response {

    private Long id;

    private String email;

    private String fullName;

    private Long user_entity_id;

    private List<response_todo> todoList;

}
