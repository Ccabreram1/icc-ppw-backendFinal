package ec.edu.ups.icc.portafolio.modules.appointments.services;

import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentRequestDto;
import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentResponseDto;
import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponseDto toDto(AppointmentEntity appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setProgrammerId(appointment.getProgrammer().getId());
        dto.setProgrammerName(appointment.getProgrammer().getName());
        dto.setProgrammerEmail(appointment.getProgrammer().getEmail());
        dto.setClientId(appointment.getClient().getId());
        dto.setClientName(appointment.getClient().getName());
        dto.setClientEmail(appointment.getClient().getEmail());
        dto.setDateTime(appointment.getDateTime());
        dto.setComment(appointment.getComment());
        dto.setStatus(appointment.getStatus().name());
        dto.setProgrammerResponse(appointment.getProgrammerResponse());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setMeetingLink(appointment.getMeetingLink());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());
        return dto;
    }

    public AppointmentEntity toEntity(AppointmentRequestDto dto) {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setDateTime(dto.getDateTime());
        appointment.setComment(dto.getComment());
        appointment.setDurationMinutes(dto.getDurationMinutes() != null ? dto.getDurationMinutes() : 60);
        appointment.setMeetingLink(dto.getMeetingLink());
        return appointment;
    }

    public void updateEntity(AppointmentRequestDto dto, AppointmentEntity appointment) {
        if (dto.getDateTime() != null) {
            appointment.setDateTime(dto.getDateTime());
        }
        if (dto.getComment() != null) {
            appointment.setComment(dto.getComment());
        }
        if (dto.getDurationMinutes() != null) {
            appointment.setDurationMinutes(dto.getDurationMinutes());
        }
        if (dto.getMeetingLink() != null) {
            appointment.setMeetingLink(dto.getMeetingLink());
        }
    }
}