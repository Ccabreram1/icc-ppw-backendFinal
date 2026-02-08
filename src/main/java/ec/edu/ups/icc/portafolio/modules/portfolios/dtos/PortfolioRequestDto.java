package ec.edu.ups.icc.portafolio.modules.portfolios.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PortfolioRequestDto {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "La especialidad es obligatoria")
    private String speciality;

    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
    private Integer yearsExperience;

    @Positive(message = "La tarifa por hora debe ser positiva")
    private Double hourlyRate;

    private Boolean isAvailable;

    // Getters y Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
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
}