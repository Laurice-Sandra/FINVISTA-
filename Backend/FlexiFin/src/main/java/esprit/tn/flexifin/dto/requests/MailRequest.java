package esprit.tn.flexifin.dto.requests;



import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MailRequest {
    private String name;
    private String to;
    private String from;
    private String subject;
}
