package ec.edu.ups.icc.portafolio.modules.availabilities.controllers;

import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityRequestDto;
import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityResponseDto;
import ec.edu.ups.icc.portafolio.modules.availabilities.services.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/programmer/{programmerId}")
    public ResponseEntity<List<AvailabilityResponseDto>> getAvailabilitiesByProgrammer(
            @PathVariable Long programmerId) {
        return ResponseEntity.ok(availabilityService.findByProgrammerId(programmerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROGRAMMER')")
    public ResponseEntity<AvailabilityResponseDto> createAvailability(
            @Valid @RequestBody AvailabilityRequestDto availabilityDto) {
        AvailabilityResponseDto created = availabilityService.create(availabilityDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @availabilitySecurity.isOwner(#id)")
    public ResponseEntity<AvailabilityResponseDto> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityRequestDto availabilityDto) {
        return ResponseEntity.ok(availabilityService.update(id, availabilityDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @availabilitySecurity.isOwner(#id)")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/programmer/{programmerId}/available")
    public ResponseEntity<List<AvailabilityResponseDto>> getAvailableSlots(
            @PathVariable Long programmerId) {
        return ResponseEntity.ok(availabilityService.findActiveByProgrammerId(programmerId));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN') or @availabilitySecurity.isOwner(#id)")
    public ResponseEntity<AvailabilityResponseDto> toggleAvailability(
            @PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.toggleStatus(id));
    }
}