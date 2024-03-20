package esprit.tn.flexifin.entities;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;



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
    private String description;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private TranStatus status;
    @Enumerated(EnumType.STRING)
    private TranType type;
    private String paymentId;

    // Association to the sender's account
    @ManyToOne
    @JoinColumn(name = "sender_account_id") // Use a descriptive column name
    private Account senderAccount;

    // Association to the receiver's account
    @ManyToOne
    @JoinColumn(name = "receiver_account_id") // Use a descriptive column name
    private Account receiverAccount;


}