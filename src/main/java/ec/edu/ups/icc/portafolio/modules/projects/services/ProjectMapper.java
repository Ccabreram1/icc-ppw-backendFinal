package ec.edu.ups.icc.portafolio.modules.projects.services;

import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectRequestDto;
import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectResponseDto;
import ec.edu.ups.icc.portafolio.modules.projects.models.ProjectEntity;
import ec.edu.ups.icc.portafolio.modules.projects.models.ProjectType;
import ec.edu.ups.icc.portafolio.modules.projects.models.ParticipationType;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponseDto toDto(ProjectEntity project) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setProjectType(project.getProjectType().name());
        dto.setParticipationType(project.getParticipationType().name());
        dto.setTechnologies(project.getTechnologies());
        dto.setRepositoryUrl(project.getRepositoryUrl());
        dto.setDemoUrl(project.getDemoUrl());
        dto.setImageUrl(project.getImageUrl());
        dto.setPortfolioId(project.getPortfolio().getId());
        dto.setPortfolioName(project.getPortfolio().getUser().getName());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }

    public ProjectEntity toEntity(ProjectRequestDto dto) {
        ProjectEntity project = new ProjectEntity();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setProjectType(ProjectType.valueOf(dto.getProjectType().toUpperCase()));
        project.setParticipationType(ParticipationType.valueOf(dto.getParticipationType().toUpperCase()));
        project.setTechnologies(dto.getTechnologies());
        project.setRepositoryUrl(dto.getRepositoryUrl());
        project.setDemoUrl(dto.getDemoUrl());
        project.setImageUrl(dto.getImageUrl());
        return project;
    }

    public void updateEntity(ProjectRequestDto dto, ProjectEntity project) {
        if (dto.getName() != null) {
            project.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            project.setDescription(dto.getDescription());
        }
        if (dto.getProjectType() != null) {
            project.setProjectType(ProjectType.valueOf(dto.getProjectType().toUpperCase()));
        }
        if (dto.getParticipationType() != null) {
            project.setParticipationType(ParticipationType.valueOf(dto.getParticipationType().toUpperCase()));
        }
        if (dto.getTechnologies() != null) {
            project.setTechnologies(dto.getTechnologies());
        }
        if (dto.getRepositoryUrl() != null) {
            project.setRepositoryUrl(dto.getRepositoryUrl());
        }
        if (dto.getDemoUrl() != null) {
            project.setDemoUrl(dto.getDemoUrl());
        }
        if (dto.getImageUrl() != null) {
            project.setImageUrl(dto.getImageUrl());
        }
    }
}