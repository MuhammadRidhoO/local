package task.company.local.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class response_todo {

    private Long Id;

    private String SubTitle;

    private String Descraption;

    private Boolean IsComplete;

    private LocalDateTime CreatedDate;

    private LocalDate FinishAt; 

    private long daysBetween;

    private Long User_Id;
}
