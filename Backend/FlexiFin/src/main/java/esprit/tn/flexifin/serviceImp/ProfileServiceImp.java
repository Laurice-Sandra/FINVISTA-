package esprit.tn.flexifin.serviceImp;


import esprit.tn.flexifin.entities.Profile;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.ProfileRepository;
import esprit.tn.flexifin.serviceInterfaces.IProfileService;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileServiceImp implements IProfileService {
    ProfileRepository profileRepository;
    IUserService iUserService;
    public List<Profile> retrieveAllProfiles(){
        return profileRepository.findAll();
    }
@Override
    public Profile addProfileAssignUser(Profile profile,Long idUser){
            User u = iUserService.retrieveUser(idUser);
            profile.setUser(u);
        return profileRepository.save(profile);
    }

    public Profile updateProfile (Profile profile){
        return profileRepository.save(profile);
    }

    public  Profile retrieveProfile (Long idProfile){
        return profileRepository.findById(idProfile).orElse(null);
    }
}

