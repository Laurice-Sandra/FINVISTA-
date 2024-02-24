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
     LoanType loantype;
    float interestRate;
     float monthlyPayment;
     float loanCost;
     RepaymentMethod repaymentMethod;




    //private float interestRate; (constant?)

    private String loanReason;
    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;
    @ManyToOne
    @JsonIgnore
    private Account account;


}