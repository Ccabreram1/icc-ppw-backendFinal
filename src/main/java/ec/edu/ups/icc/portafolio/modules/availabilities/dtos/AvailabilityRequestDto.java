package ec.edu.ups.icc.portafolio.modules.availabilities.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class AvailabilityRequestDto {
    @NotNull(message = "El ID del programador es obligatorio")
    private Long programmerId;

    @NotNull(message = "El día de la semana es obligatorio")
    private String dayOfWeek;

    @NotNull(message = "La hora de inicio es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotNull(message = "La modalidad es obligatoria")
    private String modality;

    private Boolean isActive;

    // Getters y Setters
    public Long getProgrammerId() {
        return programmerId;
    }

    public void setProgrammerId(Long programmerId) {
        this.programmerId = programmerId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}