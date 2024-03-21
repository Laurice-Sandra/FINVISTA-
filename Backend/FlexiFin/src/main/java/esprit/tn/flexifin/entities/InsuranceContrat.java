package esprit.tn.flexifin.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "insurance_contrat")
public class InsuranceContrat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;
    private float prime;
    private Date startDate= new Date(System.currentTimeMillis());
    private Date EndDate;
    @Temporal(TemporalType.DATE)
    private LocalDate dateEffet ;
    @Enumerated(EnumType.STRING)
    private TypeContrat type;
    private String designation;
    private float montant;
    @OneToOne
    private Sinister sinister;



}
