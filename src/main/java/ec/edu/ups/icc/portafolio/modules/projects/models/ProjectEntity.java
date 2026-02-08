package ec.edu.ups.icc.portafolio.modules.projects.models;

import ec.edu.ups.icc.portafolio.modules.portfolios.models.PortfolioEntity;
import ec.edu.ups.icc.portafolio.shared.entities.BaseModel;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class ProjectEntity extends BaseModel {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "project_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    @Column(name = "participation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationType participationType;

    @ElementCollection
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologies = new ArrayList<>();

    @Column(name = "repository_url")
    private String repositoryUrl;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private PortfolioEntity portfolio;

    // Constructores
    public ProjectEntity() {
    }

    public ProjectEntity(String name, String description, ProjectType projectType,
            ParticipationType participationType) {
        this.name = name;
        this.description = description;
        this.projectType = projectType;
        this.participationType = participationType;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public ParticipationType getParticipationType() {
        return participationType;
    }

    public void setParticipationType(ParticipationType participationType) {
        this.participationType = participationType;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PortfolioEntity getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioEntity portfolio) {
        this.portfolio = portfolio;
    }
}