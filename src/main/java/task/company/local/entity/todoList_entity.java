package task.company.local.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class todoList_entity {
    
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private String SubTitle;

  private String Descraption;

  private Boolean IsComplete;

  private LocalDate FinishAt;

  private LocalDateTime CreatedDate;

  @PrePersist
  protected void onCreate() {
      CreatedDate = LocalDateTime.now();
  }
}
