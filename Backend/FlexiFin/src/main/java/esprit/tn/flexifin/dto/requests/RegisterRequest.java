package esprit.tn.flexifin.dto.requests;



import esprit.tn.flexifin.entities.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    private String firstname ;
    private String lastname ;
    private String email ;
    private String password ;
    private Role role ;
}


