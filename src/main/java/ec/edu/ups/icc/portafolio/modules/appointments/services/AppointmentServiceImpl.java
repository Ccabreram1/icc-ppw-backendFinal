package ec.edu.ups.icc.portafolio.modules.appointments.services;

import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentRequestDto;
import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentResponseDto;
import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentEntity;
import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentStatus;
import ec.edu.ups.icc.portafolio.modules.appointments.repositories.AppointmentRepository;
import ec.edu.ups.icc.portafolio.modules.notifications.services.EmailService;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.modules.users.repositories.UserRepository;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.ConflictException;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;
    private final EmailService emailService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            AppointmentMapper appointmentMapper,
            EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.appointmentMapper = appointmentMapper;
        this.emailService = emailService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponseDto> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDto findById(Long id) {
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> findByProgrammerId(Long programmerId) {
        return appointmentRepository.findByProgrammerId(programmerId)
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> findByClientId(Long clientId) {
        return appointmentRepository.findByClientId(clientId)
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AppointmentResponseDto create(AppointmentRequestDto appointmentDto) {
        UserEntity programmer = userRepository.findById(appointmentDto.getProgrammerId())
                .orElseThrow(() -> new NotFoundException(
                        "Programador no encontrado con ID: " + appointmentDto.getProgrammerId()));

        UserEntity client = userRepository.findById(appointmentDto.getClientId())
                .orElseThrow(
                        () -> new NotFoundException("Cliente no encontrado con ID: " + appointmentDto.getClientId()));

        LocalDateTime dateTime = appointmentDto.getDateTime();
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("La fecha no puede ser en el pasado");
        }

        boolean hasConflict = appointmentRepository.existsByProgrammerIdAndDateTimeBetween(
                appointmentDto.getProgrammerId(),
                dateTime.minusHours(1),
                dateTime.plusHours(1));

        if (hasConflict) {
            throw new ConflictException("El programador ya tiene una cita programada en ese horario");
        }

        AppointmentEntity appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setProgrammer(programmer);
        appointment.setClient(client);
        appointment.setStatus(AppointmentStatus.PENDING);

        AppointmentEntity saved = appointmentRepository.save(appointment);

        emailService.sendAppointmentNotification(saved);

        return appointmentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public AppointmentResponseDto update(Long id, AppointmentRequestDto appointmentDto) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + id));

        if (!appointment.getStatus().equals(AppointmentStatus.PENDING)) {
            throw new BadRequestException("Solo se pueden modificar citas en estado PENDING");
        }

        appointmentMapper.updateEntity(appointmentDto, appointment);
        AppointmentEntity updated = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public AppointmentResponseDto approve(Long id, String responseMessage) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + id));

        if (!appointment.getStatus().equals(AppointmentStatus.PENDING)) {
            throw new BadRequestException("Solo se pueden aprobar citas en estado PENDING");
        }

        appointment.setStatus(AppointmentStatus.APPROVED);
        appointment.setProgrammerResponse(responseMessage);

        AppointmentEntity updated = appointmentRepository.save(appointment);

        emailService.sendAppointmentApproval(updated);

        return appointmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public AppointmentResponseDto reject(Long id, String responseMessage) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + id));

        if (!appointment.getStatus().equals(AppointmentStatus.PENDING)) {
            throw new BadRequestException("Solo se pueden rechazar citas en estado PENDING");
        }

        appointment.setStatus(AppointmentStatus.REJECTED);
        appointment.setProgrammerResponse(responseMessage);

        AppointmentEntity updated = appointmentRepository.save(appointment);

        emailService.sendAppointmentRejection(updated);

        return appointmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public AppointmentResponseDto complete(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + id));

        if (!appointment.getStatus().equals(AppointmentStatus.APPROVED)) {
            throw new BadRequestException("Solo se pueden completar citas en estado APPROVED");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        AppointmentEntity updated = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public AppointmentResponseDto cancel(Long id) {
        AppointmentEntity appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con ID: " + id));

        if (appointment.getStatus().equals(AppointmentStatus.COMPLETED) ||
                appointment.getStatus().equals(AppointmentStatus.CANCELLED)) {
            throw new BadRequestException("No se puede cancelar una cita en estado " + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        AppointmentEntity updated = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new NotFoundException("Cita no encontrada con ID: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> findUpcomingAppointments() {
        return appointmentRepository.findByDateTimeAfterAndStatusIn(
                LocalDateTime.now(),
                List.of(AppointmentStatus.PENDING, AppointmentStatus.APPROVED))
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> findByStatus(String status) {
        AppointmentStatus statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
        return appointmentRepository.findByStatus(statusEnum)
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponseDto> search(Long programmerId, Long clientId, String status,
            String startDate, String endDate, Pageable pageable) {
        if (programmerId != null) {
            return appointmentRepository.findByProgrammerId(programmerId, pageable)
                    .map(appointmentMapper::toDto);
        }

        if (clientId != null) {
            return appointmentRepository.findByClientId(clientId, pageable)
                    .map(appointmentMapper::toDto);
        }

        if (status != null) {
            AppointmentStatus statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
            return appointmentRepository.findByStatus(statusEnum, pageable)
                    .map(appointmentMapper::toDto);
        }

        return appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toDto);
    }
}