package ec.edu.ups.icc.portafolio.modules.portfolios.models;

import ec.edu.ups.icc.portafolio.modules.projects.models.ProjectEntity;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.shared.entities.BaseModel;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class PortfolioEntity extends BaseModel {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "speciality", nullable = false)
    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectEntity> projects = new ArrayList<>();

    // Constructores
    public PortfolioEntity() {
    }

    public PortfolioEntity(UserEntity user, Speciality speciality) {
        this.user = user;
        this.speciality = speciality;
    }

    // Getters y Setters
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Integer getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Integer yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public List<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectEntity> projects) {
        this.projects = projects;
    }
}