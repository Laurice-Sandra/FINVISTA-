package esprit.tn.flexifin.dto.requests;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPassword {
    private String email ;
}
