package task.company.local.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class request_todo {

    private String SubTitle;

    private String Descraption;

    private Boolean IsComplete;

    @Future(message = "FinishAt must be in the future")
    private LocalDate FinishAt;

    private Long User_Id;

}
