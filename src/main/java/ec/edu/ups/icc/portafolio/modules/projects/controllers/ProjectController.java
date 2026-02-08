package ec.edu.ups.icc.portafolio.modules.projects.controllers;

import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectRequestDto;
import ec.edu.ups.icc.portafolio.modules.projects.dtos.ProjectResponseDto;
import ec.edu.ups.icc.portafolio.modules.projects.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponseDto>> getAllProjects(Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByPortfolioId(
            @PathVariable Long portfolioId) {
        return ResponseEntity.ok(projectService.findByPortfolioId(portfolioId));
    }

    @GetMapping("/type/{projectType}")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByType(
            @PathVariable String projectType) {
        return ResponseEntity.ok(projectService.findByProjectType(projectType));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROGRAMMER')")
    public ResponseEntity<ProjectResponseDto> createProject(
            @Valid @RequestBody ProjectRequestDto projectDto) {
        ProjectResponseDto createdProject = projectService.create(projectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @projectSecurity.isPortfolioOwner(#id)")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDto projectDto) {
        return ResponseEntity.ok(projectService.update(id, projectDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @projectSecurity.isPortfolioOwner(#id)")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProjectResponseDto>> searchProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String participationType,
            @RequestParam(required = false) String technology,
            Pageable pageable) {
        return ResponseEntity.ok(projectService.search(
                name, projectType, participationType, technology, pageable));
    }

    @GetMapping("/portfolio/{portfolioId}/count")
    public ResponseEntity<Long> countProjectsByPortfolioId(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(projectService.countByPortfolioId(portfolioId));
    }
}