package ec.edu.ups.icc.portafolio.modules.projects.services;

import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectRequestDto;
import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectResponseDto;
import ec.edu.ups.icc.portafolio.modules.projects.models.ProjectEntity;
import ec.edu.ups.icc.portafolio.modules.projects.models.ProjectType;
import ec.edu.ups.icc.portafolio.modules.projects.models.ParticipationType;
import ec.edu.ups.icc.portafolio.modules.projects.repositories.ProjectRepository;
import ec.edu.ups.icc.portafolio.modules.portfolios.models.PortfolioEntity;
import ec.edu.ups.icc.portafolio.modules.portfolios.repositories.PortfolioRepository;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PortfolioRepository portfolioRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository,
            PortfolioRepository portfolioRepository,
            ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.portfolioRepository = portfolioRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto findById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> findByPortfolioId(Long portfolioId) {
        return projectRepository.findByPortfolioId(portfolioId)
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> findByProjectType(String projectType) {
        ProjectType type = ProjectType.valueOf(projectType.toUpperCase());
        return projectRepository.findByProjectType(type)
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponseDto create(ProjectRequestDto projectDto) {
        PortfolioEntity portfolio = portfolioRepository.findById(projectDto.getPortfolioId())
                .orElseThrow(
                        () -> new NotFoundException("Portafolio no encontrado con ID: " + projectDto.getPortfolioId()));

        ProjectEntity project = projectMapper.toEntity(projectDto);
        project.setPortfolio(portfolio);

        ProjectEntity savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public ProjectResponseDto update(Long id, ProjectRequestDto projectDto) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proyecto no encontrado con ID: " + id));

        projectMapper.updateEntity(projectDto, project);

        if (projectDto.getPortfolioId() != null) {
            PortfolioEntity portfolio = portfolioRepository.findById(projectDto.getPortfolioId())
                    .orElseThrow(() -> new NotFoundException(
                            "Portafolio no encontrado con ID: " + projectDto.getPortfolioId()));
            project.setPortfolio(portfolio);
        }

        ProjectEntity updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NotFoundException("Proyecto no encontrado con ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectResponseDto> search(String name, String projectType,
            String participationType, String technology,
            Pageable pageable) {
        if (name != null) {
            return projectRepository.findByNameContainingIgnoreCase(name, pageable)
                    .map(projectMapper::toDto);
        }

        if (projectType != null) {
            ProjectType type = ProjectType.valueOf(projectType.toUpperCase());
            return projectRepository.findByProjectType(type, pageable)
                    .map(projectMapper::toDto);
        }

        if (participationType != null) {
            ParticipationType participation = ParticipationType.valueOf(participationType.toUpperCase());
            return projectRepository.findByParticipationType(participation, pageable)
                    .map(projectMapper::toDto);
        }

        if (technology != null) {
            return projectRepository.findByTechnologiesContaining(technology, pageable)
                    .map(projectMapper::toDto);
        }

        return projectRepository.findAll(pageable)
                .map(projectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByPortfolioId(Long portfolioId) {
        return projectRepository.countByPortfolioId(portfolioId);
    }
}