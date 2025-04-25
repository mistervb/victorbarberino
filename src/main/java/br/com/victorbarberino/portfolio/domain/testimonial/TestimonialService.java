package br.com.victorbarberino.portfolio.domain.testimonial;

import br.com.victorbarberino.portfolio.system.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TestimonialService {
    @Autowired
    private TestimonialRepository testimonialRepository;

    public List<TestimonialData> getAllTestimonials() {
        return testimonialRepository.findAll().stream().map(TestimonialEntity::convertToData).toList();
    }

    public TestimonialData saveTestimonial(TestimonialData payload) {
        String inputType = payload.getAvatarInputType();
        byte[] avatarBytes = null;
        String avatarContentType = null;
        if ("file".equals(inputType)) {
            payload.setAvatar(null);
            if (payload.getAvatarFile() == null || payload.getAvatarFile().isEmpty()) {
                throw new IllegalArgumentException("É necessário enviar um arquivo de foto.");
            }
            try {
                avatarBytes = payload.getAvatarFile().getBytes();
                avatarContentType = payload.getAvatarFile().getContentType();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar a foto: " + e.getMessage());
            }
        } else {
            payload.setAvatarFile(null);
            if (payload.getAvatar() == null || payload.getAvatar().trim().isEmpty()) {
                throw new IllegalArgumentException("É necessário informar a URL da foto.");
            }
        }
        TestimonialEntity entity = testimonialRepository.saveAndFlush(new TestimonialEntity(payload, avatarBytes, avatarContentType));
        if ("file".equals(inputType)) {
            entity.setAvatar(montaLinkTestimonial(entity));
            entity.setWasUploaded(true);
            entity = testimonialRepository.saveAndFlush(entity);
        }
        return entity.convertToData();
    }

    public TestimonialData getTestimonialById(UUID id) {
        return testimonialRepository.findById(id)
            .map(TestimonialEntity::convertToData)
            .orElse(null);
    }

    public TestimonialData updateTestimonial(TestimonialData payload) {
        TestimonialEntity entity = testimonialRepository.findById(payload.getId()).orElse(null);
        if (entity == null) return null;
        entity.setName(payload.getName());
        entity.setRole(payload.getRole());
        entity.setQuote(payload.getQuote());

        String inputType = payload.getAvatarInputType();
        boolean hasNewFile = payload.getAvatarFile() != null && !payload.getAvatarFile().isEmpty();
        boolean hasNewUrl = payload.getAvatar() != null && !payload.getAvatar().trim().isEmpty();

        if ("file".equals(inputType) && hasNewFile) {
            entity.setAvatar(null);
            try {
                entity.setAvatarBytes(payload.getAvatarFile().getBytes());
                entity.setAvatarContentType(payload.getAvatarFile().getContentType());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar foto: " + e.getMessage());
            }
            entity = testimonialRepository.saveAndFlush(entity);
            entity.setWasUploaded(true);
            entity.setAvatar(montaLinkTestimonial(entity));
        } else if (!"file".equals(inputType) && hasNewUrl) {
            entity.setAvatarBytes(null);
            entity.setAvatarContentType(null);
            entity.setWasUploaded(false);
            entity.setAvatar(payload.getAvatar());
        }
        entity = testimonialRepository.saveAndFlush(entity);
        return entity.convertToData();
    }

    // Exclui um depoimento pelo ID
    public boolean deleteTestimonial(UUID id) {
        if (!testimonialRepository.existsById(id)) return false;
        testimonialRepository.deleteById(id);
        return true;
    }

    private String montaLinkTestimonial(TestimonialEntity testimonial) {
        return HttpUtil.getDynamicAppDomain() + "/testimonial/img?id=" + testimonial.getId();
    }
}
