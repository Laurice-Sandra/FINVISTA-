package esprit.tn.flexifin.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceContrat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;
    private float prime;
    private Date startDate;
    private Date EndDate;
    private Date dateEffet ;
    private LoanStatus loanStatus;
    @Enumerated(EnumType.STRING)
    private TypeContrat type;

    @OneToOne
    private Sinister sinister;
    @OneToOne(mappedBy = "insuranceContrat")
    private Loan loan;
}
