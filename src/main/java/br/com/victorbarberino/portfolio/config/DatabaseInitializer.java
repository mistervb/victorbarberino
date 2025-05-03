package br.com.victorbarberino.portfolio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.victorbarberino.portfolio.domain.admin.Admin;
import br.com.victorbarberino.portfolio.domain.admin.AdminRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if no admin exists
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminvb"));
            admin.setRole("ADMIN");
            admin.setActive(true);
            
            adminRepository.save(admin);
            
            System.out.println("Default admin user created.");
        }
    }
}
