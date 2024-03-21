package esprit.tn.flexifin.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Insurance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAssurance;
    private String designation;
    private float montant;
    @ManyToOne(cascade=CascadeType.ALL)
    private InsuranceContrat insuranceContrat;
    private Date startDate= new Date(System.currentTimeMillis());
    @ManyToOne
    private User user;
}
