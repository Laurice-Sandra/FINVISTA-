package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.dto.requests.UpdateUserRequest;
import esprit.tn.flexifin.entities.Insurance;
import esprit.tn.flexifin.entities.TypeContrat;
import esprit.tn.flexifin.entities.User;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.AuthenticationService;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImp implements IUserService {
    UserRepository userRepository;
    //private final AuthenticationService authenticationService ;
    private final PasswordEncoder passwordEncoder ;

    InsuranceRepository insuranceRepository;
    InsuranceContratRepository insuranceContratRepository;
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public  User updateUser(User user){
        return userRepository.save(user);
    }

    public User retrieveUser (Long idUser) {
        return userRepository.findById(idUser).orElse(null);
    }


    @Override
    public User getCurrentUser(Principal connectedUser) {
        return (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    }

    @Override
    public void updateCurrentUser(Principal connectedUser, UpdateUserRequest updatedUser) {
        User currentUser = getCurrentUser(connectedUser) ;

        if(updatedUser.getFirstname() != null) currentUser.setFirstName(updatedUser.getFirstname());  ;
        if(updatedUser.getLastname() != null) currentUser.setLastName(updatedUser.getLastname());
        if(updatedUser.getEmail() != null) currentUser.setEmail(updatedUser.getEmail());
        if(updatedUser.getPassword() != null) currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        userRepository.save(currentUser) ;
    }

    @Override
    public void deleteCurrentUser(Principal connectedUser) {
        User currentUser = getCurrentUser(connectedUser) ;
        //authenticationService.logout();
        userRepository.delete(currentUser);
    }

    @Override
    public String currentUploadDirectory( Principal connectedUser) {
        User current = getCurrentUser(connectedUser) ;
        return  "src/main/resources/user_images/"  + current.getIdUser() + current.getFirstName() ;
    }
    @Override
    public void addProfileImage(MultipartFile imageFile, Principal connectedUser) throws IOException {

        User current = getCurrentUser(connectedUser) ;
        String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        String uploadDirectory = currentUploadDirectory(connectedUser) ;
        Path uploadPath = Path.of(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        current.setImageName(uniqueFileName);
        userRepository.save(current) ;

    }

    @Override
    public byte[] getProfileImage(Principal connectedUser) throws IOException {
        String uploadDirectory = currentUploadDirectory(connectedUser) ;
        String currentUserImageName = getCurrentUser(connectedUser).getImageName() ;
        Path imagePath = Path.of(uploadDirectory, currentUserImageName);

        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            return null; // Handle missing images
        }


    }

    @Override
    public void updateProfileImage(MultipartFile imageFile, Principal connectedUser) throws IOException {
        String uploadDirectory = currentUploadDirectory(connectedUser) ;
        String currentUserImageName = getCurrentUser(connectedUser).getImageName() ;
        Path imagePath = Path.of(uploadDirectory, currentUserImageName);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);

        }
        addProfileImage(imageFile,connectedUser) ;
    }
























    @Override
        public float getMontantU(Long cinU) {
            User user = userRepository.findById(cinU).orElse(null);
            float montantInsuranceContrat = 0;
            for (Insurance ins : user.getProfileUser().getInsurances()) {
                if (ins.getInsuranceContrat().getType() == TypeContrat.Monthly) {
                    montantInsuranceContrat += ins.getMontant() * 12;
                } else if (ins.getInsuranceContrat().getType() == TypeContrat.Biannual) {
                    montantInsuranceContrat += ins.getMontant() * 2;
                } else if (ins.getInsuranceContrat().getType() == TypeContrat.Annual){
                    montantInsuranceContrat += ins.getMontant();
                }
            }
            return montantInsuranceContrat;
        }

        @Override
        public Set<User> getUserByType(TypeContrat tc) {
            return userRepository.getByAssuranceType(tc);
        }

        @Override
        public void statistiques() {

        }

        //erride
        //    public String createLoanSimulationPdf(Loan loan) throws DocumentException, FileNotFoundException {
        //        Document document = new Document();
        //        String filePath = "D:\\PIDEV PROJECT\\loan_simulation." + loan.getIdLoan() + ".pdf";
        //        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        //
        //        document.open();
        //        // Title
        //        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        //        Paragraph title = new Paragraph("LOAN CONTRACT", titleFont);
        //        title.setAlignment(Element.ALIGN_CENTER);
        //        document.add(title);
        //
        //        // Loan Information
        //        document.add(new Paragraph(" "));
        //        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12);
        //        document.add(new Paragraph("LOAN ID: " + loan.getIdLoan(), infoFont));
        //        document.add(new Paragraph("START DATE: " + formatDate(loan.getStartDate()), infoFont));
        //        LocalDate endDate = loan.getStartDate().plusMonths(loan.getDuration() * 12); // Assuming duration is in years
        //        document.add(new Paragraph("DATE OF CLOSURE: " + formatDate(endDate), infoFont));
        //        // Add other necessary loan information here
        //
        //        document.add(new Paragraph(" "));
        //
        //        List<String[]> simulationResults = simulateLoan(loan);
        //        PdfPTable table = new PdfPTable(5); // 5 columns
        //
        //        // Add header row
        //        for (String header : simulationResults.get(0)) {
        //            table.addCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        //        }
        //
        //        // Add data rows
        //        simulationResults.stream().skip(1).forEach(row -> {
        //            for (String cell : row) {
        //                table.addCell(new Phrase(cell, new Font(Font.FontFamily.HELVETICA, 12)));
        //            }
        //        });
        //
        //        document.add(table);
        //        document.close();
        //
        //        return filePath;
        //    }



}
