package br.com.victorbarberino.portfolio.domain.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ContactData {
    private UUID contactId;
    @NotBlank(message = "O nome é obrigatório")
    private String name;
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;
    @NotBlank(message = "A mensagem é obrigatória")
    private String message;
    private LocalDateTime createdAt;

    public ContactData(UUID contactId, String name, String email, String message, LocalDateTime createdAt) {
        this.contactId = contactId;
        this.name = name;
        this.email = email;
        this.message = message;
        this.createdAt = createdAt;
    }
}
