package ec.edu.ups.icc.portafolio.modules.appointments.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class AppointmentRequestDto {
    @NotNull(message = "El ID del programador es obligatorio")
    private Long programmerId;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clientId;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha debe ser en el futuro")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    private String comment;

    @Positive(message = "La duración debe ser positiva")
    private Integer durationMinutes;

    private String meetingLink;

    // Getters y Setters
    public Long getProgrammerId() {
        return programmerId;
    }

    public void setProgrammerId(Long programmerId) {
        this.programmerId = programmerId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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