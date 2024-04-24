package esprit.tn.flexifin.serviceInterfaces;

import esprit.tn.flexifin.entities.Profile;

import java.util.List;

public interface IProfileService {
    List<Profile> retrieveAllProfiles();

    Profile addProfileAssignUser(Profile profile,Long idUser);

    Profile updateProfile (Profile profile);

    Profile retrieveProfile (Long idProfile);

    float calculateLoanHistory(Long profileId);

    float calculateProfileScore(Long idProfile);

    void updateProfileScores(Long profileId);
}
