package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.User;

import java.util.List;

public interface IUserService {
    List<User> retrieveAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User retrieveUser (Long idUser);
}
