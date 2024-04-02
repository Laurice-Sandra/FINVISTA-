package esprit.tn.flexifin.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonReponse {

    private String message;
    private boolean success;
    public JsonReponse(boolean success,String message) {
        this.message = message;
        this.success=success;
    }
}
