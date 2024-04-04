package esprit.tn.flexifin.serviceInterfaces;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

public interface ILocalService {
    LocaleResolver localeResolver();
    LocaleChangeInterceptor localeChangeInterceptor();
    void addInterceptors(InterceptorRegistry registry);
}
