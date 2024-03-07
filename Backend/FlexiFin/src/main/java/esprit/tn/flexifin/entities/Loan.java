package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
     LocalDate startDate;
     Integer duration;
    @Enumerated(EnumType.STRING)
     LoanType loantype;
    float interestRate;
     float payment;
     float loanCost;
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