package esprit.tn.flexifin.serviceImp;


import esprit.tn.flexifin.entities.Profile;
import esprit.tn.flexifin.repositories.ProfileRepository;
import esprit.tn.flexifin.serviceInterfaces.IProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileServiceImp implements IProfileService {
    ProfileRepository profileRepository;
    public List<Profile> retrieveAllProfiles(){
        return profileRepository.findAll();
    }

    public Profile addProfile(Profile profile){
        return profileRepository.save(profile);
    }

    public Profile updateProfile (Profile profile){
        return profileRepository.save(profile);
    }

    public  Profile retrieveProfile (Long idProfile){
        return profileRepository.findById(idProfile).orElse(null);
    }
}

