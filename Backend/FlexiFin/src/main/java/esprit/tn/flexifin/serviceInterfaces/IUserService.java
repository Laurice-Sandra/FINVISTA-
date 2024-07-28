package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.dto.requests.UpdateUserRequest;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface IUserService {

    User getCurrentUser(Principal connectedUser) ;

     void updateCurrentUser(Principal connectedUser, UpdateUserRequest updatedUser) ;

     void deleteCurrentUser(Principal connectedUser) ;

    String currentUploadDirectory( Principal connectedUser) ;
     void addProfileImage(MultipartFile imageFile , Principal connectedUser) throws IOException;






















    List<User> retrieveAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User retrieveUser (Long idUser);






    float getMontantU(Long cinU);

    byte[] getProfileImage(Principal connectedUser) throws IOException;

    void updateProfileImage(MultipartFile imageFile, Principal connectedUser) throws IOException;

    Set<User> getUserByType(TypeContrat tc);

    //    String createUserDesignationPdf(User user) throws DocumentException, FileNotFoundException;
    //
    //
    //    List<String[]> DesignationUser(User user);

    void statistiques();
}
