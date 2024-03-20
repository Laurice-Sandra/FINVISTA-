package esprit.tn.flexifin.configurations;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StripeConfig {
        @PostConstruct
        public void setup() {
            Stripe.apiKey = "sk_test_51OwHrFGyyUC9GOwXBcaISrcPu3drDd4Rcx6MrO7ROCLp7lpzVnDzEtEUuvURveKgndwL9L9fILbenmFHJ9cjh6BI00dfnulCQQ"; // Utilisez votre clé secrète Stripe
        }


}
