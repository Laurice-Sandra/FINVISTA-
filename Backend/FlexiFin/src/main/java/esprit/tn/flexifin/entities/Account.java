package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAccount;
    private float balance;
    private LocalDate openDate;


    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private Set<Loan> loans ;
 @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> transactions = new HashSet<>();

    /*// Transactions où ce compte est l'émetteur
    @JsonIgnore
    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> outgoingTransactions;

    // Transactions où ce compte est le destinataire
    @JsonIgnore
    @OneToMany(mappedBy = "receiverAccount")
    private Set<Transaction> incomingTransactions;
*/
    @OneToOne
    @JsonIgnore
    private Profile profile;


}
