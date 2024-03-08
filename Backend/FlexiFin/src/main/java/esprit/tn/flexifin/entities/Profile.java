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
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idProfile;
    private float income;
    private String job;
    private Integer score;
    private Integer loan_history;


    @JsonIgnore
    @OneToOne(mappedBy = "profile")
    private Account account;


    @OneToOne
    @JsonIgnore
    private User user;



}