package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IUserService {
    List<User> retrieveAllUsers();

    User addUser(User u);

    User updateUser(User user);

    User retrieveUser (Long idUser);
    InsuranceContrat addInsuranceContrat (InsuranceContrat c);

    Insurance addInsurance(Insurance a, int cinU, Long idContrat);

    InsuranceContrat getContratU(Long idU);

     Date calculateEndDate(Date startDate, TypeContrat type);

    float getMontantU(int cinU);

    Set<User> getUserByType(TypeContrat tc);

    //    String createUserDesignationPdf(User user) throws DocumentException, FileNotFoundException;
    //
    //
    //    List<String[]> DesignationUser(User user);

    void statistiques();
}
