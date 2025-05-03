package br.com.victorbarberino.portfolio.domain.section;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionData {
    private UUID id;
    
    @NotBlank(message = "O código da seção é obrigatório")
    @Size(min = 3, max = 50, message = "O código deve ter entre 3 e 50 caracteres")
    private String code;
    
    @NotBlank(message = "O título da seção é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    private String title;
    
    @NotNull(message = "A visibilidade da seção é obrigatória")
    private Boolean visible;
    
    private Integer displayOrder;
    
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String description;
}
