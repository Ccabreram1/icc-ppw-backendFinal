package ec.edu.ups.icc.portafolio.modules.appointments.models;

import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.shared.entities.BaseModel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class AppointmentEntity extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programmer_id", nullable = false)
    private UserEntity programmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserEntity client;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(length = 500)
    private String comment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(name = "programmer_response", length = 500)
    private String programmerResponse;

    @Column(name = "duration_minutes")
    private Integer durationMinutes = 60;

    @Column(name = "meeting_link", length = 500)
    private String meetingLink;

    // Constructores
    public AppointmentEntity() {
    }

    public AppointmentEntity(UserEntity programmer, UserEntity client, LocalDateTime dateTime) {
        this.programmer = programmer;
        this.client = client;
        this.dateTime = dateTime;
    }

    // Getters y Setters
    public UserEntity getProgrammer() {
        return programmer;
    }

    public void setProgrammer(UserEntity programmer) {
        this.programmer = programmer;
    }

    public UserEntity getClient() {
        return client;
    }

    public void setClient(UserEntity client) {
        this.client = client;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getProgrammerResponse() {
        return programmerResponse;
    }

    public void setProgrammerResponse(String programmerResponse) {
        this.programmerResponse = programmerResponse;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }
}