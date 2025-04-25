package br.com.victorbarberino.portfolio.domain.testimonial;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestimonialRepository extends JpaRepository<TestimonialEntity, UUID> {
}
