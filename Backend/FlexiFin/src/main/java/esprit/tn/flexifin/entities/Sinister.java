package esprit.tn.flexifin.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sinister implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long idSinister;
    private Date reportDate;
    private String justification;
    private SinisterStatus sinisterStatus;

    @OneToOne(mappedBy = "sinister")
    private InsuranceContrat insuranceContrat;
}
