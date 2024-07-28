package esprit.tn.flexifin.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ImageResponse {
    private byte[] image ;
}

