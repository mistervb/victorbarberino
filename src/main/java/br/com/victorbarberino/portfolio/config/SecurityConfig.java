package br.com.victorbarberino.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.victorbarberino.portfolio.domain.admin.AdminDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/js/**", "/assets/**", "/fonts/**", "/project/img/**", "/testimonial/img/**", "/test/**").permitAll()
                .requestMatchers("/admin/**").authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/admin/sections")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
            
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(AdminDetailsService adminDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // Set the custom UserDetailsService
        authProvider.setUserDetailsService(adminDetailsService);
        
        // Set the password encoder - this will be used to compare submitted passwords with stored encrypted ones
        authProvider.setPasswordEncoder(passwordEncoder);
        
        return authProvider;
    }
    

}
