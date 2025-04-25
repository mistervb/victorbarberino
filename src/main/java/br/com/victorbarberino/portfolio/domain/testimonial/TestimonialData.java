package br.com.victorbarberino.portfolio.domain.testimonial;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialData {
    private UUID id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "O cargo/empresa é obrigatório")
    @Size(min = 3, max = 100, message = "O cargo/empresa deve ter entre 3 e 100 caracteres")
    private String role;

    @NotBlank(message = "O depoimento é obrigatório")
    @Size(min = 10, max = 1000, message = "O depoimento deve ter entre 10 e 1000 caracteres")
    private String quote;

    private transient MultipartFile avatarFile;
    private String avatar;
    private String avatarInputType;
    private boolean wasUploaded;

    // Validação customizada: garantir que pelo menos um dos campos de imagem está preenchido
    public boolean isAvatarValid() {
        boolean hasUrl = (avatar != null && !avatar.trim().isEmpty());
        boolean hasFile = (avatarFile != null && !avatarFile.isEmpty());
        return hasUrl || hasFile;
    }
}
