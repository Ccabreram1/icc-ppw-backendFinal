package ec.edu.ups.icc.portafolio.modules.availabilities.services;

import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityRequestDto;
import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityResponseDto;
import ec.edu.ups.icc.portafolio.modules.availabilities.models.AvailabilityEntity;
import ec.edu.ups.icc.portafolio.modules.availabilities.models.DayOfWeek;
import ec.edu.ups.icc.portafolio.modules.availabilities.models.Modality;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityMapper {

    public AvailabilityResponseDto toDto(AvailabilityEntity availability) {
        AvailabilityResponseDto dto = new AvailabilityResponseDto();
        dto.setId(availability.getId());
        dto.setProgrammerId(availability.getProgrammer().getId());
        dto.setProgrammerName(availability.getProgrammer().getName());
        dto.setDayOfWeek(availability.getDayOfWeek().name());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        dto.setModality(availability.getModality().name());
        dto.setIsActive(availability.getIsActive());
        dto.setCreatedAt(availability.getCreatedAt());
        dto.setUpdatedAt(availability.getUpdatedAt());
        return dto;
    }

    public AvailabilityEntity toEntity(AvailabilityRequestDto dto) {
        AvailabilityEntity availability = new AvailabilityEntity();
        availability.setDayOfWeek(DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase()));
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
        availability.setModality(Modality.valueOf(dto.getModality().toUpperCase()));
        availability.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return availability;
    }

    public void updateEntity(AvailabilityRequestDto dto, AvailabilityEntity availability) {
        if (dto.getDayOfWeek() != null) {
            availability.setDayOfWeek(DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase()));
        }
        if (dto.getStartTime() != null) {
            availability.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            availability.setEndTime(dto.getEndTime());
        }
        if (dto.getModality() != null) {
            availability.setModality(Modality.valueOf(dto.getModality().toUpperCase()));
        }
        if (dto.getIsActive() != null) {
            availability.setIsActive(dto.getIsActive());
        }
    }
}