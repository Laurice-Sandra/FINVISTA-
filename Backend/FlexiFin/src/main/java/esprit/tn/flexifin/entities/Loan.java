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
@Table(name = "loan")
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long idloan;
     float ammountRequest;
     LocalDate startDate;
     Integer duration;
//    @Enumerated(EnumType.STRING)
//     LoanType loantype;
//    float interestRate;
//     float monthlyPayment;
//     float loanCost;
//    @Enumerated(EnumType.STRING)
//    RepaymentMethod repaymentMethod;

    //private float interestRate;

    //String loanReason;

    @ManyToOne
    @JsonIgnore
    private Account account;

    @OneToOne
    private InsuranceContrat insuranceContrat;

}