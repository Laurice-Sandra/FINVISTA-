package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;

import java.util.List;

public interface IInsuranceService {
     List<Insurance> retrieveAllInsurances();
     Insurance addInsurance(Insurance i);
     Insurance updateInsurance(Insurance i);

    List<Insurance> retrieveInsurance(Long idUser);
    void delete(Long idInsurance);


    Insurance addInsurance(Insurance a, int cinU, Long idContrat);
}
