package task.company.local.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class todoList_entity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private String SubTitle;

  private String Descraption;

  private Boolean IsComplete;

  private LocalDate FinishAt;

  private LocalDateTime CreatedDate;

  @ManyToOne
  @JoinColumn(name = "user_entity_id")
  private user_entity user;

  @PrePersist
  protected void onCreate() {
    CreatedDate = LocalDateTime.now();
  }
}
