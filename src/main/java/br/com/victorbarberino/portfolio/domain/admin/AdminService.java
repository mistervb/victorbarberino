package br.com.victorbarberino.portfolio.domain.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin createAdmin(String username, String rawPassword, String role) {
        if (adminRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Ju00e1 existe um admin com o username: " + username);
        }
        
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Admin admin = new Admin(username, encodedPassword, role);
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public void changePassword(Long id, String newPassword) {
        adminRepository.findById(id).ifPresent(admin -> {
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
        });
    }

    public void toggleAdminStatus(Long id) {
        adminRepository.findById(id).ifPresent(admin -> {
            admin.setActive(!admin.isActive());
            adminRepository.save(admin);
        });
    }

    public boolean deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
