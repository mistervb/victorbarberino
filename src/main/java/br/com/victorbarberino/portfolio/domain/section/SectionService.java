package br.com.victorbarberino.portfolio.domain.section;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<SectionData> getAllSections() {
        return sectionRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(SectionEntity::toData)
                .collect(Collectors.toList());
    }

    public List<SectionData> getVisibleSections() {
        return sectionRepository.findByVisibleTrueOrderByDisplayOrderAsc()
                .stream()
                .map(SectionEntity::toData)
                .collect(Collectors.toList());
    }

    public SectionData getSectionById(UUID id) {
        return sectionRepository.findById(id)
                .map(SectionEntity::toData)
                .orElseThrow(() -> new RuntimeException("Seção não encontrada com ID: " + id));
    }

    public SectionData getSectionByCode(String code) {
        return sectionRepository.findByCode(code)
                .map(SectionEntity::toData)
                .orElseThrow(() -> new RuntimeException("Seção não encontrada com código: " + code));
    }

    public boolean isSectionVisible(String code) {
        return sectionRepository.findByCode(code)
                .map(SectionEntity::isVisible)
                .orElse(false);
    }

    public SectionData updateSection(UUID id, SectionData sectionData) {
        SectionEntity entity = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seção não encontrada com ID: " + id));
        
        // Em seções fixas, não permitimos alterar o código
        // apenas o título, visibilidade e ordem
        entity.setTitle(sectionData.getTitle());
        entity.setVisible(sectionData.getVisible());
        entity.setDisplayOrder(sectionData.getDisplayOrder() != null ? sectionData.getDisplayOrder() : entity.getDisplayOrder());
        entity.setDescription(sectionData.getDescription());
        
        entity = sectionRepository.save(entity);
        return entity.toData();
    }

    public void toggleSectionVisibility(UUID id) {
        SectionEntity entity = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seção não encontrada com ID: " + id));
        
        entity.setVisible(!entity.isVisible());
        sectionRepository.save(entity);
    }
    
    // Método para inicializar as seções fixas do template
    @PostConstruct
    public void initializeDefaultSections() {
        // Verificar e criar seções padrão se não existirem
        createDefaultSectionIfNotExists("home", "Home", 10);
        createDefaultSectionIfNotExists("sobre", "Sobre Mim", 20);
        createDefaultSectionIfNotExists("servicos", "Serviços", 30);
        createDefaultSectionIfNotExists("projetos", "Projetos", 40);
        createDefaultSectionIfNotExists("depoimentos", "Depoimentos", 50);
        createDefaultSectionIfNotExists("contato", "Contato", 60);
    }
    
    private void createDefaultSectionIfNotExists(String code, String title, int order) {
        if (!sectionRepository.existsByCode(code)) {
            SectionEntity section = new SectionEntity();
            section.setCode(code);
            section.setTitle(title);
            section.setVisible(true);
            section.setDisplayOrder(order);
            section.setDescription("Seção " + title + " do portfólio");
            
            sectionRepository.save(section);
        }
    }
}
