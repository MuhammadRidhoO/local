package task.company.local.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Getter
public class response_user_login {

    private Long Id;

    private String FullName;

    private String Email;

    private List<response_todo_to_user> Todo_List_Entities;
}
