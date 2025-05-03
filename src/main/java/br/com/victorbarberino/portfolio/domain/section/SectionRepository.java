package br.com.victorbarberino.portfolio.domain.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, UUID> {
    List<SectionEntity> findByVisibleTrue();
    List<SectionEntity> findAllByOrderByDisplayOrderAsc();
    List<SectionEntity> findByVisibleTrueOrderByDisplayOrderAsc();
    Optional<SectionEntity> findByCode(String code);
    boolean existsByCode(String code);
}
