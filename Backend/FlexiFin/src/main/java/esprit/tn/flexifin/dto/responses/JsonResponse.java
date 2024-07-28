package esprit.tn.flexifin.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonResponse {

    private String message;
    private boolean success;
    public JsonResponse(boolean success,String message) {
        this.message = message;
        this.success=success;
    }
}
