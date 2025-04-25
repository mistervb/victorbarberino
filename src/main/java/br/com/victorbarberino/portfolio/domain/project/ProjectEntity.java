package br.com.victorbarberino.portfolio.domain.project;

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
    private String image;

    @Lob
    private byte[] imageBytes;

    private String imageContentType;

    private List<String> tags;

    @Column(name = "was_uploaded", nullable = false)
    private boolean wasUploaded = false;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    public ProjectEntity(ProjectData data) {
        this.title = data.getTitle();
        this.description = data.getDescription();
        this.image = data.getImage();
        this.tags = data.getTags();
        this.createdAt = LocalDateTime.now();
    }

    public ProjectEntity(ProjectData data, byte[] imageBytes, String imageContentType) {
        this.title = data.getTitle();
        this.description = data.getDescription();
        this.image = data.getImage();
        this.tags = data.getTags();
        this.imageBytes = imageBytes;
        this.imageContentType = imageContentType;
        this.createdAt = LocalDateTime.now();
    }

    public ProjectData convertToData() {
        ProjectData data = new ProjectData();
        data.setProjectId(this.id);
        data.setTitle(this.title);
        data.setDescription(this.description);
        data.setImage(this.image);
        data.setTags(this.tags);
        data.setWasUploaded(this.wasUploaded);
        return data;
    }
}
