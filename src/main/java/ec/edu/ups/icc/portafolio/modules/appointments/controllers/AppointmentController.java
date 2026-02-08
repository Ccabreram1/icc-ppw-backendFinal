package ec.edu.ups.icc.portafolio.modules.appointments.controllers;

import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentRequestDto;
import ec.edu.ups.icc.portafolio.modules.appointments.dtos.AppointmentResponseDto;
import ec.edu.ups.icc.portafolio.modules.appointments.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN ve todas las citas
    public ResponseEntity<Page<AppointmentResponseDto>> getAllAppointments(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @GetMapping("/programmer/{programmerId}")
    @PreAuthorize("hasRole('ADMIN') or @appointmentSecurity.isProgrammer(#programmerId)")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByProgrammer(
            @PathVariable Long programmerId) {
        return ResponseEntity.ok(appointmentService.findByProgrammerId(programmerId));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or @appointmentSecurity.isClient(#clientId)")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByClient(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.findByClientId(clientId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROGRAMMER')")
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @Valid @RequestBody AppointmentRequestDto appointmentDto) {
        AppointmentResponseDto created = appointmentService.create(appointmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or @appointmentSecurity.isProgrammerForAppointment(#id)")
    public ResponseEntity<AppointmentResponseDto> approveAppointment(
            @PathVariable Long id,
            @RequestParam(required = false) String responseMessage) {
        return ResponseEntity.ok(appointmentService.approve(id, responseMessage));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or @appointmentSecurity.isProgrammerForAppointment(#id)")
    public ResponseEntity<AppointmentResponseDto> rejectAppointment(
            @PathVariable Long id,
            @RequestParam(required = false) String responseMessage) {
        return ResponseEntity.ok(appointmentService.reject(id, responseMessage));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN') or @appointmentSecurity.isProgrammerForAppointment(#id)")
    public ResponseEntity<AppointmentResponseDto> completeAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.complete(id));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @appointmentSecurity.isClientForAppointment(#id) or @appointmentSecurity.isProgrammerForAppointment(#id)")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancel(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentResponseDto>> getUpcomingAppointments() {
        return ResponseEntity.ok(appointmentService.findUpcomingAppointments());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(appointmentService.findByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AppointmentResponseDto>> searchAppointments(
            @RequestParam(required = false) Long programmerId,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Pageable pageable) {
        return ResponseEntity.ok(appointmentService.search(
                programmerId, clientId, status, startDate, endDate, pageable));
    }
}