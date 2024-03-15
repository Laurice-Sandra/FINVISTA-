package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile")
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idProfile;
    private float income;
    private String job;

    private Integer score;
    @JsonIgnore
    @OneToOne(mappedBy = "profile")
    private Account account ;


    @OneToOne
    @JsonIgnore
    private User user;



}
