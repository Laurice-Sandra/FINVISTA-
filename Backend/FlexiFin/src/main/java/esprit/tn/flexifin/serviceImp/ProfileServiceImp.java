package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Profile;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.LoanRepository;
import esprit.tn.flexifin.repositories.ProfileRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileServiceImp implements IProfileService {
    ProfileRepository profileRepository;
    UserRepository userRepository;
    LoanRepository loanRepository;
    private static final float BASE_SCORE = 300;
    private static final float INCOME_MULTIPLIER = 0.1f; // Ajustez en fonction de vos critères
    private static final float BONUS_FOR_NO_LOANS = 100;

    public List<Profile> retrieveAllProfiles() {
        return profileRepository.findAll();
    }

    @Override
    public Profile addProfileAssignUser(Profile profile, Long idUser) {
        User u = userRepository.findById(idUser).orElse(null);
        profile.setUser(u);
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile retrieveProfile(Long idProfile) {
        return profileRepository.findById(idProfile).orElse(null);
    }

    public float calculateLoanHistory(Long profileId) {

        int totalLoans = loanRepository.getTotalLoansByProfileId(profileId);
        float totalAmountBorrowed = loanRepository.getTotalAmountBorrowedByProfileId(profileId);
        float totalAmountRepaid = loanRepository.getTotalAmountRepaidByProfileId(profileId);
        int totalClosedLoans = loanRepository.getTotalClosedLoansByProfileId(profileId);

        float repaymentRatioScore = totalAmountBorrowed > 0 ? (totalAmountRepaid / totalAmountBorrowed) * 1000 : 0;

        float closedLoanRatioScore = totalLoans > 0 ? ((float) totalClosedLoans / totalLoans) * 1000 : BONUS_FOR_NO_LOANS;


        float finalScore = repaymentRatioScore + closedLoanRatioScore;

        Profile profile = profileRepository.findById(profileId).orElse(null);
        if (profile != null) {
            profile.setLoan_history((int) finalScore);
            profileRepository.save(profile);
        }
        return finalScore;

    }

    public float calculateProfileScore(Long idProfile) {
        Profile p = profileRepository.findById(idProfile).orElse(null);
        float loanHistoryScore = p.getLoan_history();
        float incomeScore = p.getIncome() * INCOME_MULTIPLIER;

        return BASE_SCORE + loanHistoryScore + incomeScore  ;
    }
    @Override
    public void updateProfileScores(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found with ID: " + profileId));

        float hist = calculateLoanHistory(profileId);
        float score = calculateProfileScore(profileId);

        profile.setLoan_history((int) hist);
        profile.setScore((int) score);

        profileRepository.save(profile);
    }



}




