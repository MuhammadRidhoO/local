package task.company.local.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class response_todo_to_user {
    
    private Long Id;

    private String SubTitle;

    private String Descraption;

    private Boolean IsComplete;

    private LocalDateTime CreatedDate;

    private LocalDate FinishAt; 

    private long daysBetween;
}
