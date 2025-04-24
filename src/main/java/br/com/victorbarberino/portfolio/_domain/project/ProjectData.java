package br.com.victorbarberino.portfolio._domain.project;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectData {
    private UUID projectId;
    
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String title;
    
    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 500, message = "A descrição deve ter entre 10 e 500 caracteres")
    private String description;

    private transient MultipartFile imageFile;

    private String image;

    private List<String> tags = new ArrayList<>();

    private String imageInputType;

    private boolean wasUploaded;

    public void setTags(List<String> tags) {
        if (tags != null && tags.size() == 1 && "[]".equals(tags.get(0))) {
            this.tags = new ArrayList<>();
        } else {
            this.tags = tags;
        }
    }

    // Validação customizada: garantir que pelo menos um dos campos de imagem está preenchido
    public boolean isImageValid() {
        boolean hasUrl = (image != null && !image.trim().isEmpty());
        boolean hasFile = (imageFile != null && !imageFile.isEmpty());
        return hasUrl || hasFile;
    }
}
