package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private int cin;
    private String firstName;
    private String lastName;
    private String adress;
    private Long phoneNum;
    private String profession;
    private float salary;
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Profile profileUser;
    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
        private List<Insurance> insurances;


}
