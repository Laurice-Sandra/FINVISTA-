package esprit.tn.flexifin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;
    private Integer amount;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Enumerated(EnumType.STRING)
    private TranStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account;






   /* @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private TranType type;
    private String paymentId;
    private String description;*/

   /* // Association to the sender's account
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id") // Use a descriptive column name
    private Account senderAccount;

    // Association to the receiver's account
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_account_id") // Use a descriptive column name
    private Account receiverAccount;*/


}