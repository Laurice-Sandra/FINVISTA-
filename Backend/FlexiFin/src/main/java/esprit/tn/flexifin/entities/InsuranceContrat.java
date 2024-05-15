package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    private Date endDate;
    @Temporal(TemporalType.DATE)
    private LocalDate dateEffet ;
    @Enumerated(EnumType.STRING)
    private TypeContrat type;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "insuranceContrat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Insurance> insuranceList;



}
