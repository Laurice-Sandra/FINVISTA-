package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Beneficiary;
import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.InsuranceContrat;
import esprit.tn.flexifin.entities.TypeContrat;

import java.util.List;
import java.util.Set;

public interface IBeneficiaryService {
    Beneficiary addBeneficiary(Beneficiary bf);

    List<Beneficiary> retrieveAllBeneficiary();

    InsuranceContrat addInsuranceContrat (InsuranceContrat c);

    Insurance addInsurance(Insurance a, int cinBf, Long idContrat);

    InsuranceContrat getContratBf(Long idBf);

    float getMontantBf(int cinBf);

    Set<Beneficiary> getBeneficaryByType(TypeContrat tc);

    void statistiques();
}
