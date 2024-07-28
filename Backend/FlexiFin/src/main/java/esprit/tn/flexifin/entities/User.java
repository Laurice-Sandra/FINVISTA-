package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private String firstName;

    private LocalDate birthDate;

    private String lastName;

    private String adress;
    private String email;

    private String phoneNum;
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Profile profileUser;
}
