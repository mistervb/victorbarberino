package br.com.victorbarberino.portfolio._domain.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vb_projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String description;

    @Lob
    @Column(nullable = false)
    private String image;

    private List<String> tags;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public ProjectEntity(ProjectData data) {
        this.title = data.getTitle();
        this.description = data.getDescription();
        this.image = data.getImage();
        this.tags = data.getTags();
        this.createdAt = LocalDateTime.now();
    }

    public ProjectData convertToData() {
        System.out.println(new ProjectData(this.id, this.title, this.description, this.image, this.tags).getTags());
        return new ProjectData(this.id, this.title, this.description, this.image, this.tags);
    }
}
