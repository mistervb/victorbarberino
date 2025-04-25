package br.com.victorbarberino.portfolio.domain.contact;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vb_contacts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contact_id")
    private UUID contactId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @Lob
    private String message;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ContactEntity(ContactData data) {
        this.name = data.getName();
        this.email = data.getEmail();
        this.message = data.getMessage();
        this.createdAt = LocalDateTime.now();
    }

    public ContactData convertToData() {
        return new ContactData(
            this.contactId,
            this.name,
            this.email,
            this.message,
            this.createdAt
        );
    }
}
