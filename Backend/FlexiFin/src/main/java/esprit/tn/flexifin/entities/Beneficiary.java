package esprit.tn.flexifin.entities;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Beneficiary implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idBeneficiaire;
    private int cin;
    private String nom;
    private String prenom;
    private String profession;
    private float salaire;
    @JsonIgnore
    @OneToMany(mappedBy="beneficiary", fetch=FetchType.EAGER)
    private List<Insurance> insurances;
}
