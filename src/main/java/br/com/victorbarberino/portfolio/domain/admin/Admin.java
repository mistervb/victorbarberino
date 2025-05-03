package br.com.victorbarberino.portfolio.domain.admin;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    @Lob
    private String password;
    
    @Column(nullable = false)
    private String role;
    
    private boolean active = true;
    
    // Constructor without id field for creating new admins
    public Admin(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert role to Spring Security GrantedAuthority
        // Format: "ROLE_" + role (Spring Security convention)
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
    
    @Override
    public boolean isAccountNonExpired() {
        // Account is never expired in this implementation
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Account is never locked in this implementation
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Use the active field to determine if the account is enabled
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Credentials never expire in this implementation
        return true;
    }
}
