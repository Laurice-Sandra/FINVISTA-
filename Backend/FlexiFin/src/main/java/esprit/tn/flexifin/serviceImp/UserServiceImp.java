package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements IUserService {
    UserRepository userRepository;
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public  User updateUser(User user){
        return userRepository.save(user);
    }

    public User retrieveUser (Long idUser){
        return userRepository.findById(idUser).orElse(null);

    }
}
