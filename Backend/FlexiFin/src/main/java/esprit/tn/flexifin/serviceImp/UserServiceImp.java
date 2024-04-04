package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.*;
import esprit.tn.flexifin.repositories.InsuranceContratRepository;
import esprit.tn.flexifin.repositories.InsuranceRepository;
import esprit.tn.flexifin.repositories.UserRepository;
import esprit.tn.flexifin.serviceInterfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImp implements IUserService {
    UserRepository userRepository;
    InsuranceRepository insuranceRepository;
    InsuranceContratRepository insuranceContratRepository;


    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User retrieveUser(Long idUser) {
        return userRepository.findById(idUser).orElse(null);

    }








    @Override
    public float getMontantU(int cinU) {
        User user = userRepository.getByCin(cinU);
        float montantInsuranceContrat = 0;
        for (Insurance ins : user.getInsurances()) {
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