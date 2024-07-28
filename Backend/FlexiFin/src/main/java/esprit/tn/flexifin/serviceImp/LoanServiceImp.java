package esprit.tn.flexifin.serviceImp;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stripe.exception.StripeException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import esprit.tn.flexifin.entities.*;
import esprit.tn.flexifin.repositories.*;
import esprit.tn.flexifin.serviceInterfaces.IAccountService;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;
import esprit.tn.flexifin.serviceInterfaces.IProfileService;
import esprit.tn.flexifin.serviceInterfaces.ITransactionService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
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
    IAccountService iAccountService;
    ITransactionService iTransactionService;
    ProfileRepository profileRepository;
    TransactionRepository transactionRepository;
    AppSettingRepository appSettingRepository;
    UserRepository userRepository;

    private final JavaMailSender emailSender;
    private FreeMarkerConfigurer freemarkerConfigurer;

    // I-Fixing Loan TMM
    @Override
    public double updateTmm(double newTmm) {
        AppSetting appSetting = appSettingRepository.findFirstByOrderByIdAsc().orElse(new AppSetting());
        appSetting.setTmm(newTmm);
        appSettingRepository.save(appSetting);
        return appSetting.getTmm();
    }
    private double getInterestRate() {
        return appSettingRepository.findFirstByOrderByIdAsc()
                .map(setting -> setting.getTmm() + setting.getFixedInterestPart())
                .orElseThrow(() -> new IllegalStateException("AppSetting is not configured."));
    }
    //II-CRUD AND FILTERS

    public Loan addLoan(Loan loan){
        return loanRepository.save(loan);
    }
    @Override
    public Loan addLoanAssignAccount(Loan loan, Long idAccount) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + idAccount));

        loan.setLoanStatus(LoanStatus.Pending);
        loan.setInterestRate(getInterestRate());
        loan.setAccount(account);

        if (account.getProfile() != null) {
            iProfileService.updateProfileScores(account.getProfile().getIdProfile());

        }
        return loanRepository.save(loan);
    }
    @Override
    public List<Loan> retrieveAllLoans(){
        return loanRepository.findAll();
    }
    @Override
    public Loan retrieveLoan (Long idLoan){
        return loanRepository.findById(idLoan).orElse(null);
    }
    @Override
    public void removeLoan(Long idLoan){

        loanRepository.deleteById(idLoan);
    }
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

    //III-LOAN SIMULATION
    @Override
    public double calculateLoanCapacity(double monthlyIncome, double monthlyDebtPayments, double monthlyExpenses) {
        return monthlyIncome - (monthlyDebtPayments + monthlyExpenses);
    }

    @Override
    public double calculatePayment(float amount, double interestRate, int totalPeriods) {
        return (amount * interestRate) / (1 - Math.pow(1 + interestRate, -totalPeriods));
    }

    @Override
    public List<String[]> simulateLoanCombined(Loan loan) {
        double interestRate = loan.getInterestRate();
        RepaymentMethod repaymentMethod = loan.getRepaymentMethod();
        int totalPeriods = repaymentMethod == RepaymentMethod.MENSUALITY ? loan.getDuration() * 12 : loan.getDuration();
        double periodicInterestRate = repaymentMethod == RepaymentMethod.MENSUALITY ? interestRate / 12 : interestRate;

        double constantPayment = loan.getLoantype() == LoanType.PERSONAL ? calculatePayment(loan.getAmmountRequest(), periodicInterestRate, totalPeriods) : 0;
        double constantAmortization = loan.getLoantype() == LoanType.AGRICULTURE ? loan.getAmmountRequest() / totalPeriods : 0;

        List<String[]> simulationResults = new ArrayList<>();
        String periodLabel = repaymentMethod == RepaymentMethod.MENSUALITY ? "Month" : "Year";
        simulationResults.add(new String[]{periodLabel, "Remaining Balance", "Amortization", "Interest", "Payment"});

        double remainingBalance = loan.getAmmountRequest();
        double totalLoanCost = 0;
        double firstPayment = 0;


        for (int period = 1; period <= totalPeriods; period++) {
            double interestForPeriod = remainingBalance * periodicInterestRate;
            double amortization = 0;
            double payment = 0;

            switch (loan.getLoantype()) {
                case PERSONAL -> {
                    payment = constantPayment;
                    amortization = payment - interestForPeriod;
                }
                case AGRICULTURE -> {
                    payment = constantAmortization + interestForPeriod;
                    amortization = constantAmortization;
                }
                case BUSINESS -> {
                    if (period == totalPeriods) {
                        amortization = remainingBalance; // Entire balance is paid at the end
                    }
                    payment = interestForPeriod + amortization;
                }
            }
            if (period == 1) {
                firstPayment = payment; // Capture only the first payment
            }

            simulationResults.add(new String[]{
                    String.format("%s %d", periodLabel, period),
                    String.format("%.2f", remainingBalance),
                    String.format("%.2f", amortization),
                    String.format("%.2f", interestForPeriod),
                    String.format("%.2f", payment)
            });

            remainingBalance -= amortization;
            totalLoanCost  += interestForPeriod;
        }

        // Directly adjust loan details

        loan.setLoanCost(totalLoanCost );
        loan.setRemainingBalance((float)(loan.getAmmountRequest()+totalLoanCost));
        loan.setPayment(firstPayment);
        loanRepository.save(loan);

        return simulationResults;
    }




    //IV-PDF GENERATION
    @Override
    public String createLoanSimulationPdf(Loan loan) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        AppSetting appSetting = appSettingRepository.findFirstByOrderByIdAsc().orElse(new AppSetting());

        String filePath = appSetting.getFilePath() + loan.getIdLoan() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();
        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
        Paragraph title = new Paragraph("LOAN CONTRACT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // User Information
        String userName = loan.getAccount().getProfile().getUser().getFirstName() + " " + loan.getAccount().getProfile().getUser().getLastName();
        Font userFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, BaseColor.DARK_GRAY);
        Paragraph userParagraph = new Paragraph("Borrower: " + userName, userFont);
        document.add(userParagraph);

        //LoanSimulation
        List<String[]> simulationResults = simulateLoanCombined(loan);

        // Loan Information
        document.add(new Paragraph(" "));
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        document.add(new Paragraph("LOAN ID: " + loan.getIdLoan(), infoFont));
        document.add(new Paragraph("START DATE: " + formatDate(loan.getStartDate()), infoFont));
        LocalDate endDate = loan.getStartDate().plusMonths(loan.getDuration() * 12); // Assuming duration is in years
        document.add(new Paragraph("DATE OF CLOSURE: " + formatDate(endDate), infoFont));
        document.add(new Paragraph("LOAN COST: $" + String.format("%.2f", loan.getLoanCost()), infoFont));

        document.add(new Paragraph(" "));


        PdfPTable table = new PdfPTable(5); // 5 columns

        // Style for table headers
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerBackgroundColor = new BaseColor(0, 121, 182); // A blue color

        // Add header row
        for (String header : simulationResults.get(0)) {
            PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
            cell.setBackgroundColor(headerBackgroundColor);
            table.addCell(cell);
        }

        // Add data rows
        Font tableBodyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        simulationResults.stream().skip(1).forEach(row -> {
            for (String cell : row) {
                table.addCell(new Phrase(cell, tableBodyFont));
            }
        });

        document.add(table);
        document.close();
        return filePath;
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private LocalDate calculateFirstDueDate(LocalDate startDate, RepaymentMethod repaymentMethod) {
        return repaymentMethod == RepaymentMethod.MENSUALITY ? startDate.plusMonths(1) : startDate.plusYears(1);
    }

    // V-EMAIL SENDING
    @Override
    public void sendEmailWithFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel, String attachmentPath, String templateName) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("noreply@finvistaflexifin.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);

        // Configuration de FreeMarker
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate(templateName);
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        messageHelper.setText(htmlBody, true);

        // Ajout de la pièce jointe si nécessaire
        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            FileSystemResource file = new FileSystemResource(attachmentPath);
            messageHelper.addAttachment(file.getFilename(), file);
        }

        emailSender.send(mimeMessage);
    }

    //VI-LOAN APPROVAL AND REJECTION

    private String conditionforLoanApproval(Loan loan) {

        double monthlyPayment = calculatePayment(loan.getAmmountRequest(), loan.getInterestRate(), loan.getDuration() * 12);
        double monthlyIncome = loan.getAccount().getProfile().getIncome();
        boolean isPaymentLessThanFortyPercentOfIncome=   monthlyPayment < (0.4 * monthlyIncome);
        Profile profile = loan.getAccount().getProfile();
        if (profile.getLoan_history() <= 0) {
            return "You currently have an outstanding loan.";
        } else if (!isPaymentLessThanFortyPercentOfIncome) {
            return "The monthly payment exceeds 40% of your salary.";
        } else if (profile.getScore() <= 500) {
            return "Your profile score is below the required minimum.";
        }
        return "";
    }

    @Override
    public String approveLoanByIdWithFreemarker(Long loanId) throws DocumentException, MessagingException, IOException, TemplateException {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);

        if (!loanOpt.isPresent()) {
            return "Loan with ID " + loanId + " does not exist.";
        }

        Loan loan = loanOpt.orElse(null);
        if (loan.getLoanStatus() != LoanStatus.Pending) {
            return "Loan is not in PENDING status.";
        }

        if (loan.getAccount() == null || loan.getAccount().getProfile() == null || loan.getAccount().getProfile().getUser() == null) {
            return "Account/Profile/User information is missing for loan ID " + loanId;
        }

        String rejectionReason = conditionforLoanApproval(loan);
        if (!rejectionReason.isEmpty()) {
            return rejectionReason;
        }

        loan.setStartDate(LocalDate.now());
        loan.setNextPaymentDueDate(calculateFirstDueDate(loan.getStartDate(), loan.getRepaymentMethod()));
        simulateLoanCombined(loan);
        loan.setLoanStatus(LoanStatus.Approved);
        loanRepository.save(loan);

        // Chemin vers le contrat de prêt généré
        String contractPath = createLoanSimulationPdf(loan);

        // Préparation du modèle pour FreeMarker
        Map<String, Object> templateModel = new HashMap<>();
        User user = loan.getAccount().getProfile().getUser();
        templateModel.put("name", user.getFirstName()); // Ensure this matches your User entity's method to get the name
        templateModel.put("confirmationUrl", "http://yourconfirmationlink.com"); // Adjust this to your actual confirmation link
        templateModel.put("contractPath", contractPath);

        // Appel du service d'envoi d'email modifié pour utiliser FreeMarker
        String to = user.getEmail();
        String template ="email-template.ftl";
        sendEmailWithFreemarkerTemplate(to, "Loan Application Update", templateModel, contractPath,template);

        return "Loan with ID " + loanId + " has been approved. Contract sent to: " + to;
    }

    //LOAN REJECTION
    private void rejectLoanAndNotifyUser(Long loanId) throws MessagingException, IOException, TemplateException {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found with ID: " + loanId));
        String reason = conditionforLoanApproval(loan);
        loan.setLoanStatus(LoanStatus.Denied);
        loanRepository.save(loan);

        Map<String, Object> templateModel = new HashMap<>();
        User user = loan.getAccount().getProfile().getUser();
        templateModel.put("user", user);
        templateModel.put("name", user.getFirstName());
        templateModel.put("reason", reason);
        String to = user.getEmail();

        sendEmailWithFreemarkerTemplate(to, "Loan Application Update", templateModel, null, "loan-rejection.ftl");
    }

    //SCHEDULED LOAN APPROVAL AND REJECTION
    @Override
    @Scheduled(cron = "0 0 0 */2 * *") // At 00:00 AM, every 2 days
    @Transactional
    public void processPendingLoansUpdated() throws DocumentException, MessagingException, IOException, TemplateException {
        List<Loan> pendingLoans = loanRepository.findByLoanStatus(LoanStatus.Pending);
        for (Loan loan : pendingLoans) {
            String reason = conditionforLoanApproval(loan);

            if (reason.isEmpty()) {
                // All conditions satisfied, approve the loan
                approveLoanByIdWithFreemarker(loan.getIdLoan());
            } else {
                // Any condition not satisfied, reject the loan and send email with the reason
                rejectLoanAndNotifyUser(loan.getIdLoan());
            }
        }
    }

    //SCHEDULED UPDATE PaymentDueDates
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

    //VII-LOAN CONFIRMATION
    @Override
    public void confirmLoan(Long idLoan) {
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if (loan != null && loan.getLoanStatus() == LoanStatus.Approved) {
            loan.setLoanStatus(LoanStatus.InProgress);
            loanRepository.save(loan);
        }
    }

    /*//VIII-PAYMENT SERVICE FOR LOAN
    @Override
    public Transaction processLoanTransactionWithSpecificLoan(Long senderAccountId, Long receiverAccountId, Transaction paymentRequest, Long loanId) throws StripeException {

        Transaction processedTransaction = iAccountService.processTransactionAndAdjustBalance(senderAccountId,receiverAccountId,paymentRequest);
        Loan loan = loanRepository.findById(loanId).orElse(null);

        if (processedTransaction.getType() == TranType.LOAN_REPAYMENT) {
            if (loan.getAccount().getIdAccount().equals(senderAccountId) && (loan.getLoanStatus() == LoanStatus.InProgress || loan.getLoanStatus() == LoanStatus.Default)) {
                loan.setRemainingBalance(loan.getRemainingBalance() - paymentRequest.getAmount());
                if (loan.getRemainingBalance() <= 0) {
                    loan.setLoanStatus(LoanStatus.Repaid);
                }
                loanRepository.save(loan);
            }
        } else if (processedTransaction.getType() == TranType.LOAN_DISBURSEMENT) {
            if (loan.getAccount().getIdAccount().equals(receiverAccountId) && loan.getLoanStatus() == LoanStatus.Approved) {
                loan.setLoanStatus(LoanStatus.InProgress);
                loanRepository.save(loan);
            }
        }


        if (processedTransaction.getStatus() == TranStatus.COMPLETED) {
            switch (processedTransaction.getType()) {
                case LOAN_REPAYMENT:
                    loan.setRemainingBalance(loan.getRemainingBalance() - processedTransaction.getAmount());
                    if (loan.getRemainingBalance() <= 0) {
                        loan.setLoanStatus(LoanStatus.Repaid);
                    }
                    break;

                case LOAN_DISBURSEMENT:
                    if (loan.getLoanStatus() == LoanStatus.Approved) {
                        loan.setLoanStatus(LoanStatus.InProgress);
                    }
                    break;

                default:
                    // Gérer d'autres types de transactions si nécessaire
                    break;

            }
        }

        // Sauvegarder la transaction mise à jour dans la base de données
        return processedTransaction;
    }*/

    //IX-SCHEDULED LOAN PASS TO DEFAULT
    @Scheduled(cron = "0 0 12 * * ?") // Executes every day at noon
    public void updateLoansToDefault() {
        List<Loan> loans = retrieveAllLoans();
        LocalDate today = LocalDate.now();
        loans.forEach(loan -> {
            if (loan.getNextPaymentDueDate() != null &&
                    loan.getNextPaymentDueDate().isBefore(today) &&
                    loan.getRemainingBalance() > 0) {
                loan.setLoanStatus(LoanStatus.Default);
                loanRepository.save(loan);
            }
        });
    }

    //X-LOAN NOTIFICATIONS
    @Scheduled(cron = "* 12 * * * ?") // Executed every day at noon
    public void sendPreDueDateNotifications() {
        List<Loan> loans = loanRepository.findAllByLoanStatusAndNextPaymentDueDateIsBefore(LoanStatus.InProgress, LocalDate.now().plusWeeks(1));
        for (Loan loan : loans) {
            sendPreDueDateNotification(loan.getIdLoan());
        }
    }
    @Override
    public void sendPreDueDateNotification(Long idLoan) {
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        String formattedDate = formatDate(loan.getNextPaymentDueDate());
        String message = "Reminder: Your next loan payment of $" + loan.getPayment() + " is due on " + formattedDate + ". Please ensure sufficient funds are available.";
        String userPhoneNumber = loan.getAccount().getProfile().getUser().getPhoneNum();
        String twilioPhoneNumber = "+14433992522"; // Replace with your Twilio number
        try {
            Message.creator(
                    new PhoneNumber(userPhoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    message
            ).create();

            System.out.println("Message envoyé avec succès. ");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du message: " + e.getMessage());
        }
    }

}
