package esprit.tn.flexifin.controllers;

import esprit.tn.flexifin.serviceInterfaces.ILocalService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@RestController
@AllArgsConstructor
@RequestMapping("/greeting")
public class PageController {
  ILocalService iLocalService;
    @GetMapping("/LocalResolver")
    public LocaleResolver localeResolver() {
        return iLocalService.localeResolver();
    }
    @GetMapping("/LocalInterceptor")
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return iLocalService.localeChangeInterceptor();
    }
    @PostMapping("/Interceptor")
    public void addInterceptors(InterceptorRegistry registry) {
        iLocalService.addInterceptors(registry);
    }

    @GetMapping("/international")
        public String getInternationalPage() {
            return "international";
        }


}
