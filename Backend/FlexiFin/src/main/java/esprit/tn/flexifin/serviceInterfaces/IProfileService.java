package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Profile;

import java.util.List;

public interface IProfileService {
    List<Profile> retrieveAllProfiles();

    Profile addProfile(Profile profile);

    Profile updateProfile (Profile profile);

    Profile retrieveProfile (Long idProfile);
}
