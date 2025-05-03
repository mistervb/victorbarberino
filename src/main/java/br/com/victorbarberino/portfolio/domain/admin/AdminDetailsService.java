package br.com.victorbarberino.portfolio.domain.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminDetailsService.class);

    private final AdminRepository adminRepository;

    @Autowired
    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Tentando carregar o usuário: {}", username);
        
        try {
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin não encontrado com o username: " + username));
    
            logger.debug("Usuário encontrado: {} com senha [{} caracteres]", admin.getUsername(), admin.getPassword().length());
            
            if (!admin.isActive()) {
                logger.debug("Usuário {} está inativo", username);
                throw new UsernameNotFoundException("Admin está inativo: " + username);
            }
    
            // Admin now directly implements UserDetails, so we can return it directly
            return admin;
        } catch (Exception e) {
            logger.error("Erro ao carregar usuário: {}", e.getMessage(), e);
            throw e;
        }
    }
}
