package ec.edu.ups.icc.portafolio.modules.appointments.services;

import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentRequestDto;
import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentService {
    Page<AppointmentResponseDto> findAll(Pageable pageable);

    AppointmentResponseDto findById(Long id);

    List<AppointmentResponseDto> findByProgrammerId(Long programmerId);

    List<AppointmentResponseDto> findByClientId(Long clientId);

    AppointmentResponseDto create(AppointmentRequestDto appointmentDto);

    AppointmentResponseDto update(Long id, AppointmentRequestDto appointmentDto);

    AppointmentResponseDto approve(Long id, String responseMessage);

    AppointmentResponseDto reject(Long id, String responseMessage);

    AppointmentResponseDto complete(Long id);

    AppointmentResponseDto cancel(Long id);

    void delete(Long id);

    List<AppointmentResponseDto> findUpcomingAppointments();

    List<AppointmentResponseDto> findByStatus(String status);

    Page<AppointmentResponseDto> search(Long programmerId, Long clientId, String status,
            String startDate, String endDate, Pageable pageable);
}