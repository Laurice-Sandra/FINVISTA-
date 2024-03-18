package esprit.tn.flexifin.serviceImp;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import esprit.tn.flexifin.entities.*;
import esprit.tn.flexifin.repositories.AccountRepository;
import esprit.tn.flexifin.repositories.LoanRepository;
import esprit.tn.flexifin.repositories.ProfileRepository;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;
import esprit.tn.flexifin.serviceInterfaces.IProfileService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
@AllArgsConstructor
@EnableScheduling
public class LoanServiceImp implements ILoanService {
    LoanRepository loanRepository;
    AccountRepository accountRepository;
    IProfileService iProfileService;
    ProfileRepository profileRepository;

    private static float tmm = 0.08f; // Valeur initiale du TMM, peut être configurée
    private static final float FIXED_PART = 0.12F; // Partie fixe

    private final JavaMailSender emailSender;
    private FreeMarkerConfigurer freemarkerConfigurer;










    @Override
    public void updateTmm(float newTmm) {
        LoanServiceImp.tmm = newTmm;
    }

    private float getInterestRate() {

        return tmm + FIXED_PART;
    }
@Override
    public List<Loan> retrieveAllLoans(){
        return loanRepository.findAll();
    }

    public Loan addLoan(Loan loan){

        return loanRepository.save(loan);
    }
@Override
public Loan addLoanAssignAccount(Loan loan, Long idAccount) {
    Optional<Account> accountOpt = accountRepository.findById(idAccount);

    if (!accountOpt.isPresent()) {
        // Gestion de compte non trouvé, retourner le prêt tel quel ou null
        return null; // ou gestion alternative
    }

    Account account = accountOpt.orElse(null);
    loan.setInterestRate(getInterestRate());
    loan.setRemainingBalance(loan.getAmmountRequest());
    loan.setNextPaymentDueDate(calculateFirstDueDate(loan.getStartDate(), loan.getRepaymentMethod()));
    // Assurez-vous que cette méthode existe et retourne un taux valide
    loan.setAccount(account);

    // Continuez seulement si le compte a un profil associé
    if (account.getProfile() != null) {
        Long profileId = account.getProfile().getIdProfile();

        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.orElse(null);

            // Calcul du score et de l'historique de prêt, puis mise à jour du profil
            float hist = iProfileService.calculateLoanHistory(profileId);
            float score = iProfileService.calculateProfileScore(profileId);

            profile.setLoan_history((int) hist);
            profile.setScore((int) score);

            profileRepository.save(profile); // Sauvegardez les modifications du profil
        }
    }

    return loanRepository.save(loan); // Sauvegardez et retournez le prêt
}
@Override
    public  Loan updateLoan (Loan loan){
        Optional<Loan> contratOptional = loanRepository.findById(loan.getIdLoan());
        if (contratOptional.isPresent()) {
            Loan loanToUpdate = contratOptional.orElse(null);
            loanToUpdate.setLoanStatus(LoanStatus.InProgress);
            return loanRepository.save(loanToUpdate);
        }

        return loanRepository.save(loan);
    }
@Override
    public Loan retrieveLoan (Long idLoan){
        return loanRepository.findById(idLoan).orElse(null);
    }
@Override
    public void removeLoan(Long idLoan){

        loanRepository.deleteById(idLoan);
    }

    //SEMI DEFINE SERVICES (FILTERS)
    @Override
   public List<Loan> getLoanByStatus(LoanStatus status){
return loanRepository.findByLoanStatus(status);
   }
    @Override
    public List<Loan> getLoanByStartDate(LocalDate date){
        return loanRepository.findByStartDate(date);
    }
    @Override
    public List<Loan> getLoanByLoanType(LoanType loantype){
        return loanRepository.findByLoantype(loantype);
    }
    @Override
    public List<Loan> getLoanByUserId(Long idUser){
        return loanRepository.findByUserId(idUser);
    }



@Override
    public double calculateLoanCapacity(double monthlyIncome, double monthlyDebtPayments, double monthlyExpenses) {
        return monthlyIncome - (monthlyDebtPayments + monthlyExpenses);
    }
@Override
    public List<String[]> simulateLoan(Loan loan) {
        float interestRate = loan.getInterestRate();
        RepaymentMethod repaymentMethod = loan.getRepaymentMethod();
        int totalPeriods = repaymentMethod == RepaymentMethod.MENSUALITY ? loan.getDuration() * 12 : loan.getDuration();
        float periodicInterestRate = repaymentMethod == RepaymentMethod.MENSUALITY ? interestRate / 12 : interestRate;
        double payment = calculatePayment(loan.getAmmountRequest(), periodicInterestRate, totalPeriods);

        List<String[]> simulationResults = new ArrayList<>();
        String periodLabel = repaymentMethod == RepaymentMethod.MENSUALITY ? "Month" : "Year";
        simulationResults.add(new String[]{periodLabel, "Remaining Balance", "Amortization", "Interest", "Payment"});

        float remainingBalance = loan.getAmmountRequest();
        float totalLoanCost = 0;

        for (int period = 1; period <= totalPeriods; period++) {
            float interestForPeriod = remainingBalance * periodicInterestRate;
            float amortization = (float) (payment - interestForPeriod);


            simulationResults.add(new String[]{
                    String.format("%s %d", periodLabel, period),
                    String.format("%.2f", remainingBalance),
                    String.format("%.2f", amortization),
                    String.format("%.2f", interestForPeriod),
                    String.format("%.2f", payment)
            });
            remainingBalance -= amortization;
            totalLoanCost += interestForPeriod;
        }

        loan.setLoanCost(totalLoanCost);
        loan.setPayment((float) payment);
        // Sauvegardez l'entité Loan avec le coût total du prêt et payment mis à jour
        loanRepository.save(loan);

        return simulationResults;
    }



    public double calculatePayment(float amount, float interestRate, int totalPeriods) {
        return (amount * interestRate) / (1 - Math.pow(1 + interestRate, -totalPeriods));
    }


@Override
    public String createLoanSimulationPdf(Loan loan) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        String filePath = "D:\\PIDEV PROJECT\\loan_simulation." + loan.getIdLoan() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();
        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("LOAN CONTRACT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Loan Information
        document.add(new Paragraph(" "));
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12);
        document.add(new Paragraph("LOAN ID: " + loan.getIdLoan(), infoFont));
        document.add(new Paragraph("START DATE: " + formatDate(loan.getStartDate()), infoFont));
        LocalDate endDate = loan.getStartDate().plusMonths(loan.getDuration() * 12); // Assuming duration is in years
        document.add(new Paragraph("DATE OF CLOSURE: " + formatDate(endDate), infoFont));
        // Add other necessary loan information here

        document.add(new Paragraph(" "));

        List<String[]> simulationResults = simulateLoan(loan);
        PdfPTable table = new PdfPTable(5); // 5 columns

        // Add header row
        for (String header : simulationResults.get(0)) {
            table.addCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        }

        // Add data rows
        simulationResults.stream().skip(1).forEach(row -> {
            for (String cell : row) {
                table.addCell(new Phrase(cell, new Font(Font.FontFamily.HELVETICA, 12)));
            }
        });

        document.add(table);
        document.close();

        return filePath;
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
@Override
    public String approveLoan(Long loanId) throws DocumentException, FileNotFoundException {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);


        if (!loanOpt.isPresent()) {
            return "Loan with ID " + loanId + " does not exist.";
        }

        Loan loan = loanOpt.orElse(null);
        if (loan.getLoanStatus()!= LoanStatus.Pending) {
            return "Loan is not in PENDING status.";
        }

        simulateLoan(loan);

        // Mettre à jour le statut du prêt à APPROVED après la simulation réussie
        loan.setLoanStatus(LoanStatus.Approved);
        loanRepository.save(loan);

        // Générer le contrat de prêt après l'approbation
        String contractPath = createLoanSimulationPdf(loan);

        // Vous pouvez stocker le chemin du contrat dans le prêt ou effectuer d'autres actions nécessaires

        return "Loan with ID " + loanId + " has been approved and contract generated at: " + contractPath;
    }



    private LocalDate calculateFirstDueDate(LocalDate startDate, RepaymentMethod repaymentMethod) {
        return repaymentMethod == RepaymentMethod.MENSUALITY ? startDate.plusMonths(1) : startDate.plusYears(1);
    }

    @Scheduled(cron = "0 1 3 * * ?") // Exécute cette méthode tous les jours à 13:00
    @Transactional
    @Override
    public void updatePaymentDueDates() {
        List<Loan> loans = loanRepository.findByLoanStatus(LoanStatus.InProgress); // Trouvez tous les prêts en cours
        LocalDate today = LocalDate.now();
        for (Loan loan : loans) {
            // Calculez directement la date de fin en fonction de la méthode de remboursement
            LocalDate loanEndDate = loan.getRepaymentMethod() == RepaymentMethod.MENSUALITY
                    ? loan.getStartDate().plusMonths(loan.getDuration())
                    : loan.getStartDate().plusYears(loan.getDuration());

            if (loan.getNextPaymentDueDate() != null && !loan.getNextPaymentDueDate().isAfter(today)
                    && loan.getRemainingBalance() > 0 && loan.getNextPaymentDueDate().isBefore(loanEndDate)) {

                // Mise à jour de la date d'échéance pour le prochain paiement
                loan.setNextPaymentDueDate(loan.getRepaymentMethod() == RepaymentMethod.MENSUALITY
                        ? loan.getNextPaymentDueDate().plusMonths(1)
                        : loan.getNextPaymentDueDate().plusYears(1));

                loanRepository.save(loan);
            }
        }
    }
@Override
    public void confirmLoan(Long idLoan) {
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if (loan != null && loan.getLoanStatus() == LoanStatus.Approved) {
            loan.setLoanStatus(LoanStatus.InProgress);
            loanRepository.save(loan);
        }
    }

    //EMAIL SENDING WITH FREEMARKER




    public void sendEmailWithFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel, String attachmentPath) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("noreply@finvistaflexifin.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);

        // Configuration de FreeMarker
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("email-template.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        messageHelper.setText(htmlBody, true);

        // Ajout de la pièce jointe si nécessaire
        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            FileSystemResource file = new FileSystemResource(attachmentPath);
            messageHelper.addAttachment(file.getFilename(), file);
        }

        emailSender.send(mimeMessage);
    }




    @Override
    public String approveLoanByIdWithFreemarker(Long loanId) throws DocumentException, MessagingException, IOException, TemplateException {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);

        if (!loanOpt.isPresent()) {
            return "Loan with ID " + loanId + " does not exist.";
        }

        Loan loan = loanOpt.get();
        if (loan.getLoanStatus() != LoanStatus.Pending) {
            return "Loan is not in PENDING status.";
        }

        if (loan.getAccount() == null || loan.getAccount().getProfile() == null || loan.getAccount().getProfile().getUser() == null) {
            return "Account/Profile/User information is missing for loan ID " + loanId;
        }

        simulateLoan(loan);

        loan.setLoanStatus(LoanStatus.Approved);
        loanRepository.save(loan);

        // Chemin vers le contrat de prêt généré
        String contractPath = createLoanSimulationPdf(loan);

        // Préparation du modèle pour FreeMarker
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("loan", loan);
        User user = loan.getAccount().getProfile().getUser();
        templateModel.put("user", user);
        templateModel.put("name", user.getFirstName()); // Ensure this matches your User entity's method to get the name
        templateModel.put("confirmationUrl", "http://yourconfirmationlink.com"); // Adjust this to your actual confirmation link
        templateModel.put("contractPath", contractPath);

        // Appel du service d'envoi d'email modifié pour utiliser FreeMarker
        String to = user.getEmail();
        sendEmailWithFreemarkerTemplate(to, "Confirmation de prêt", templateModel, contractPath);

        return "Loan with ID " + loanId + " has been approved. Contract sent to: " + to;
    }


}





