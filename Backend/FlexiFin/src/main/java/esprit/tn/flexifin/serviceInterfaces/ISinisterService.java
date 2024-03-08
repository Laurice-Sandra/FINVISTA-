package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Sinister;

import java.util.List;

public interface ISinisterService {
    List<Sinister> retrieveAllSinisters();

    Sinister addSinister(Sinister sinister);

    Sinister updateSinister(Sinister sinister);

    Sinister retrieveSinister (Long idSinister);

}

