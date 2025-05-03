package br.com.victorbarberino.portfolio.domain.section;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private boolean visible;
    
    @Column
    private int displayOrder;
    
    @Column(length = 500)
    private String description;
    
    public SectionData toData() {
        SectionData data = new SectionData();
        data.setId(this.id);
        data.setCode(this.code);
        data.setTitle(this.title);
        data.setVisible(this.visible);
        data.setDisplayOrder(this.displayOrder);
        data.setDescription(this.description);
        return data;
    }
    
    public static SectionEntity fromData(SectionData data) {
        SectionEntity entity = new SectionEntity();
        entity.setId(data.getId());
        entity.setCode(data.getCode());
        entity.setTitle(data.getTitle());
        entity.setVisible(data.getVisible() != null ? data.getVisible() : false);
        entity.setDisplayOrder(data.getDisplayOrder() != null ? data.getDisplayOrder() : 0);
        entity.setDescription(data.getDescription());
        return entity;
    }
}
