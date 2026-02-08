package ec.edu.ups.icc.portafolio.modules.availabilities.models;

import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.shared.entities.BaseModel;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "availabilities")
public class AvailabilityEntity extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programmer_id", nullable = false)
    private UserEntity programmer;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "modality", nullable = false)
    @Enumerated(EnumType.STRING)
    private Modality modality;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Constructores
    public AvailabilityEntity() {
    }

    public AvailabilityEntity(UserEntity programmer, DayOfWeek dayOfWeek,
            LocalTime startTime, LocalTime endTime, Modality modality) {
        this.programmer = programmer;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modality = modality;
    }

    // Getters y Setters
    public UserEntity getProgrammer() {
        return programmer;
    }

    public void setProgrammer(UserEntity programmer) {
        this.programmer = programmer;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
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

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}