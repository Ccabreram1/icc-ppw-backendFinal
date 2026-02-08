package ec.edu.ups.icc.portafolio.modules.availabilities.services;

import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityRequestDto;
import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityResponseDto;

import java.util.List;

public interface AvailabilityService {
    List<AvailabilityResponseDto> findByProgrammerId(Long programmerId);

    List<AvailabilityResponseDto> findActiveByProgrammerId(Long programmerId);

    AvailabilityResponseDto create(AvailabilityRequestDto availabilityDto);

    AvailabilityResponseDto update(Long id, AvailabilityRequestDto availabilityDto);

    void delete(Long id);

    AvailabilityResponseDto toggleStatus(Long id);
}