package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idLoan;
    float ammountRequest;
    LocalDate requestDate = LocalDate.now();
    LocalDate startDate;
    Integer duration;
    @Enumerated(EnumType.STRING)
    LoanType loantype;
    double interestRate;
    double payment;
    double loanCost;
    @Enumerated(EnumType.STRING)
    RepaymentMethod repaymentMethod;
    String loanReason;
    @Enumerated(EnumType.STRING)
    LoanStatus loanStatus;
    private float remainingBalance; // Solde restant à rembourser
    private LocalDate nextPaymentDueDate;
    @ManyToOne
    @JsonIgnore
    private Account account;


}