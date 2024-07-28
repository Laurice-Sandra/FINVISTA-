
package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Sinister;
import esprit.tn.flexifin.serviceInterfaces.ISinisterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class SinisterServiceImp implements ISinisterService {
    @Override
    public List<Sinister> retrieveAllSinisters() {
        return null;
    }

    @Override
    public Sinister addSinister(Sinister sinister) {
        return null;
    }

    @Override
    public Sinister updateSinister(Sinister sinister) {
        return null;
    }

    @Override
    public Sinister retrieveSinister(Long idSinister) {
        return null;
    }
}
