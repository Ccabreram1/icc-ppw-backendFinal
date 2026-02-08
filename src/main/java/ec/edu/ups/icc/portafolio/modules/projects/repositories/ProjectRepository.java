package ec.edu.ups.icc.portafolio.modules.projects.repositories;

import ec.edu.ups.icc.portafolio.modules.projects.models.ProjectEntity;
import ec.edu.ups.icc.portafolio.modules.projects.models.ProjectType;
import ec.edu.ups.icc.portafolio.modules.projects.models.ParticipationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByPortfolioId(Long portfolioId);

    List<ProjectEntity> findByProjectType(ProjectType projectType);

    Page<ProjectEntity> findByProjectType(ProjectType projectType, Pageable pageable);

    Page<ProjectEntity> findByParticipationType(ParticipationType participationType, Pageable pageable);

    Page<ProjectEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM ProjectEntity p WHERE :technology MEMBER OF p.technologies")
    Page<ProjectEntity> findByTechnologiesContaining(@Param("technology") String technology, Pageable pageable);

    Long countByPortfolioId(Long portfolioId);
}