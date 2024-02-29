package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.*;
import esprit.tn.flexifin.repositories.AccountRepository;
import esprit.tn.flexifin.repositories.LoanRepository;
import esprit.tn.flexifin.serviceInterfaces.ILoanService;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class LoanServiceImp implements ILoanService {
    LoanRepository loanRepository;
    AccountRepository accountRepository;

    public List<Loan> retrieveAllLoans(){
        return loanRepository.findAll();
    }

    public Loan addLoan(Loan loan){
        return loanRepository.save(loan);
    }

    public Loan addLoanAssignAccount(Loan loan, Long idAccount){
            Account account = accountRepository.findById(idAccount).orElse(null);
            loan.setAccount(account);
        return loanRepository.save(loan);
    }

    public  Loan updateLoan (Loan loan){
        Optional<Loan> contratOptional = loanRepository.findById(loan.getIdLoan());
        if (contratOptional.isPresent()) {
            Loan loanToUpdate = contratOptional.orElse(null);
            loanToUpdate.setLoanStatus(LoanStatus.InProgress);
            return loanRepository.save(loanToUpdate);
        }

        return loanRepository.save(loan);
    }

    public Loan retrieveLoan (Long idLoan){
        return loanRepository.findById(idLoan).orElse(null);
    }

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



   //LOAN APROVAL

    /*public boolean approveLoan(Long userId) {
        Profile profile = profileRepository.findByUserId(userId);
        // Vérifiez les informations nécessaires dans le profil
        if (profile != null && *//* Vérification des informations *//*) {
            // Approuver le prêt
            return true;
        }
        return false;
    }*/






    //SERVICE
    //Simulation de pret

    /*public String generateTemporaryId() {
        // Génère un ID unique temporaire
        return UUID.randomUUID().toString();
    }*/

    /*public Map<String, Float> simulateLoan(float amount, int duration, float interestRate) {
        float monthlyPayment = calculateMonthlyPayment(amount, duration, interestRate);
        float totalCost = monthlyPayment * duration;

        Map<String, Float> result = new HashMap<>();
        result.put("monthlyPayment", monthlyPayment);
        result.put("totalCost", totalCost);

        return result;

    }*/

    /*private float calculateMonthlyPayment(float amount, int duration, float interestRate) {
        float monthlyInterestRate = interestRate / 12 / 100;
        return (amount * monthlyInterestRate) / (1 - (float)Math.pow(1 + monthlyInterestRate, -duration));
    }*/

    public double calculateLoanCapacity(double monthlyIncome, double monthlyDebtPayments, double monthlyExpenses) {
        return monthlyIncome - (monthlyDebtPayments + monthlyExpenses);
    }


public Map<String, Float> simulateLoan(Loan loan) {
    Map<String, Float> loanSimulation = new LinkedHashMap<>();
    float annualInterestRate = loan.getInterestRate();
    double annualPayment = (loan.getAmmountRequest() * annualInterestRate) / (1 - Math.pow(1 + annualInterestRate, -loan.getDuration()));
    float remainingBalance = loan.getAmmountRequest();
    float totalLoanCost = 0;

    for (int year = 1; year <= loan.getDuration(); year++) {
        float interestForYear = annualInterestRate * remainingBalance;
        float yearlyAmortization = (float) (annualPayment - interestForYear);

        loanSimulation.put("Year " + year + " - Number", (float) year);
        loanSimulation.put("Year " + year + " - Remaining Balance", remainingBalance);
        loanSimulation.put("Year " + year + " - Amortization", yearlyAmortization);
        loanSimulation.put("Year " + year + " - Interest", interestForYear);
        loanSimulation.put("Year " + year + " - Annuity", (float) annualPayment);


        totalLoanCost += interestForYear;

        if (year < loan.getDuration()) {
            remainingBalance -= yearlyAmortization;
        }
    }




    loanSimulation.put("Total Loan Cost", totalLoanCost);

    return loanSimulation;
}

    @Override
    public Map<String, Float> simulateLoan2(@NotNull Loan loan) {
        Map<String, Float> loanSimulation = new LinkedHashMap<>();
        float annualInterestRate = loan.getInterestRate();
        float monthlyInterestRate = annualInterestRate / 12;
        int totalMonths = loan.getDuration() * 12;
        double monthlyPayment = (loan.getAmmountRequest() * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -totalMonths));
        float remainingBalance = loan.getAmmountRequest();
        float totalLoanCost = 0;

        for (int month = 1; month <= totalMonths; month++) {
            float interestForMonth = monthlyInterestRate * remainingBalance;
            float monthlyAmortization = (float) (monthlyPayment - interestForMonth);

            loanSimulation.put("Month " + month + " - Remaining Balance", remainingBalance);
            loanSimulation.put("Month " + month + " - Amortization", monthlyAmortization);
            loanSimulation.put("Month " + month + " - Interest", interestForMonth);
            loanSimulation.put("Month " + month + " - Monthly Payment", (float) monthlyPayment);

            remainingBalance -= monthlyAmortization;
            totalLoanCost += interestForMonth;
        }

        loanSimulation.put("Total Loan Cost", totalLoanCost);

        return loanSimulation;
    }

    @Override
    public Map<String, Float> simulateLoanWithConstantAmortizationPerYear(@NotNull Loan loan) {
        Map<String, Float> loanSimulation = new LinkedHashMap<>();
        float annualInterestRate = loan.getInterestRate();
        int totalYears = loan.getDuration();
        float totalLoanCost = 0;

        float yearlyAmortization = loan.getAmmountRequest() / totalYears;
        float remainingBalance = loan.getAmmountRequest();

        for (int year = 1; year <= totalYears; year++) {
            float interestForYear = annualInterestRate * remainingBalance;
            float annualPayment = yearlyAmortization + interestForYear;

            loanSimulation.put("Year " + year + " - Remaining Balance", remainingBalance);
            loanSimulation.put("Year " + year + " - Amortization", yearlyAmortization);
            loanSimulation.put("Year " + year + " - Interest", interestForYear);
            loanSimulation.put("Year " + year + " - Annual Payment", annualPayment);

            totalLoanCost += interestForYear;
            remainingBalance -= yearlyAmortization;
        }

        loanSimulation.put("Total Loan Cost", totalLoanCost);

        return loanSimulation;
    }
    @Override
    public Map<String, Float> simulateLoanWithConstantAmortizationPerMonth(Loan loan) {
        Map<String, Float> loanSimulation = new LinkedHashMap<>();
        float annualInterestRate = loan.getInterestRate();
        int totalMonths = loan.getDuration() * 12;
        float totalLoanCost = 0;

        float monthlyAmortization = loan.getAmmountRequest() / totalMonths;
        float remainingBalance = loan.getAmmountRequest();

        for (int month = 1; month <= totalMonths; month++) {
            float monthlyInterest = (annualInterestRate / 12) * remainingBalance;
            float monthlyPayment = monthlyAmortization + monthlyInterest;

            loanSimulation.put("Month " + month + " - Remaining Balance", remainingBalance);
            loanSimulation.put("Month " + month + " - Amortization", monthlyAmortization);
            loanSimulation.put("Month " + month + " - Interest", monthlyInterest);
            loanSimulation.put("Month " + month + " - Monthly Payment", monthlyPayment);

            totalLoanCost += monthlyInterest;
            remainingBalance -= monthlyAmortization;
        }

        loanSimulation.put("Total Loan Cost", totalLoanCost);

        return loanSimulation;
    }
    @Override
    public Map<String, Float> simulateLoanInFineByYear(@NotNull Loan loan) {
        Map<String, Float> loanSimulation = new LinkedHashMap<>();
        float annualInterestRate = loan.getInterestRate();
        float interestForYear = annualInterestRate * loan.getAmmountRequest();
        float totalInterest = interestForYear * loan.getDuration();
        loanSimulation.put( "  Yearly Interest", interestForYear);
        loanSimulation.put("Total Loan", totalInterest);
        return loanSimulation;
    }

    @Override
    public Map<String, Float> simulateLoanInFineByMonth(@NotNull Loan loan) {
        Map<String, Float> loanSimulation = new LinkedHashMap<>();
        float monthlyInterestRate = loan.getInterestRate() / 12;
        float monthlyInterest = monthlyInterestRate * loan.getAmmountRequest();
        float totalInterest = monthlyInterest * loan.getDuration() * 12;
        loanSimulation.put( "  Monthly Interest", monthlyInterest);
        loanSimulation.put("Total LoanCost", totalInterest);
        return loanSimulation;
    }

@Override
//generation de pdf
public void generatePdf(@NotNull LinkedHashMap<String, Float> loanSimulation) throws IOException {
    PDDocument document = new PDDocument();
    PDPage page = new PDPage();
    document.addPage(page);
    PDPageContentStream contentStream = new PDPageContentStream(document, page);
    contentStream.setFont(PDType1Font.HELVETICA, 12);

    float y = page.getMediaBox().getHeight() - 20; // Position verticale initiale
    float x = 50; // Position horizontale initiale
    float columnWidth = 150; // Largeur des colonnes

    contentStream.beginText();
    contentStream.newLineAtOffset(x, y);

    int currentYear = 0; // Indice du year actuel
    for (Map.Entry<String, Float> entry : loanSimulation.entrySet()) {
        String key = entry.getKey();
        String value = String.format("%.2f", entry.getValue());
        if (key.startsWith("Year")) {
            int yearIndex = Integer.parseInt(key.substring(5, key.indexOf(" -"))); // Extraire l'indice du year
            if (yearIndex != currentYear) { // Nouvelle ligne pour chaque changement d'indice de year
                currentYear = yearIndex;
                contentStream.newLineAtOffset(-x, -15); // Déplacer vers la gauche pour commencer une nouvelle ligne
            }
        }
        contentStream.showText(key + " : " + value);
        contentStream.newLineAtOffset(columnWidth, 0); // Déplacement horizontal pour la prochaine colonne
    }
    contentStream.endText();

    contentStream.close();
    document.save("D:\\PIDEV PROJECT\\loan_simulation.pdf");
    document.close();
}



}
