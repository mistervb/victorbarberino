package br.com.victorbarberino.portfolio.domain.testimonial;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import java.util.UUID;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vb_testimonials")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Lob
    @Column(nullable = false)
    private String quote;

    @Lob
    private String avatar;

    @Lob
    private byte[] avatarBytes;

    private String avatarContentType;

    @Column(name = "was_uploaded", nullable = false)
    private boolean wasUploaded = false;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public TestimonialEntity(TestimonialData data, byte[] avatarBytes, String avatarContentType) {
        this.name = data.getName();
        this.role = data.getRole();
        this.quote = data.getQuote();
        this.avatar = data.getAvatar();
        this.avatarBytes = avatarBytes;
        this.avatarContentType = avatarContentType;
        this.wasUploaded = "file".equals(data.getAvatarInputType());
        this.createdAt = LocalDateTime.now();
    }

    public TestimonialData convertToData() {
        TestimonialData data = new TestimonialData();
        data.setId(this.id);
        data.setName(this.name);
        data.setRole(this.role);
        data.setQuote(this.quote);
        data.setAvatar(this.avatar);
        data.setWasUploaded(this.wasUploaded);
        return data;
    }
}
