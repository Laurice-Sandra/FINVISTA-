package esprit.tn.flexifin.serviceImp;

import esprit.tn.flexifin.entities.Loan;
import esprit.tn.flexifin.entities.LoanStatus;
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
import java.util.*;

@Service
@AllArgsConstructor
public class LoanServiceImp implements ILoanService {
    LoanRepository loanRepository;

    public List<Loan> retrieveAllLoans(){
        return loanRepository.findAll();
    }

    public Loan addLoan(Loan loan){
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

/*
    public Map<String, Float> simulateLoan(Loan loan) {
        Map<String, Float> loanSimulation = new HashMap<>();
        float monthlyInterestRate = loan.getInterestRate() / 12;
        double monthlyPayment = (loan.getAmmountRequest() * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -loan.getDuration()));
        float remainingBalance = loan.getAmmountRequest();
        float totalLoanCost = 0;

        for (int year = 1; year <= loan.getDuration(); year++) {
            float yearlyAmortization = 0;
            float yearlyInterest = 0;
            for (int month = 1; month <= 12; month++) {
                double monthlyInterest = remainingBalance * monthlyInterestRate;
                double monthlyAmortization = monthlyPayment - monthlyInterest;
                if (remainingBalance < monthlyAmortization) {
                    monthlyAmortization = remainingBalance;
                }
                remainingBalance -= monthlyAmortization;
                yearlyAmortization += monthlyAmortization;
                yearlyInterest += monthlyInterest;
            }
            loanSimulation.put("Year " + year + " - Amortization", yearlyAmortization);
            loanSimulation.put("Year " + year + " - Interest", yearlyInterest);
            loanSimulation.put("Year " + year + " - Monthly Payment", (float) monthlyPayment);
            loanSimulation.put("Year " + year + " - Remaining Balance", remainingBalance);
            totalLoanCost += yearlyInterest;
        }

        loanSimulation.put("Total Loan Cost", totalLoanCost);

        return loanSimulation;
    }*/
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
