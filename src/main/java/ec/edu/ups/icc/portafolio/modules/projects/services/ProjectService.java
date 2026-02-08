package ec.edu.ups.icc.portafolio.modules.projects.services;

import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectRequestDto;
import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    Page<ProjectResponseDto> findAll(Pageable pageable);

    ProjectResponseDto findById(Long id);

    List<ProjectResponseDto> findByPortfolioId(Long portfolioId);

    List<ProjectResponseDto> findByProjectType(String projectType);

    ProjectResponseDto create(ProjectRequestDto projectDto);

    ProjectResponseDto update(Long id, ProjectRequestDto projectDto);

    void delete(Long id);

    Page<ProjectResponseDto> search(String name, String projectType,
            String participationType, String technology,
            Pageable pageable);

    Long countByPortfolioId(Long portfolioId);
}