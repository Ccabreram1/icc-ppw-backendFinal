package ec.edu.ups.icc.portafolio.modules.availabilities.services;

import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityRequestDto;
import ec.edu.ups.icc.portafolio.modules.availabilities.dtos.AvailabilityResponseDto;
import ec.edu.ups.icc.portafolio.modules.availabilities.models.AvailabilityEntity;
import ec.edu.ups.icc.portafolio.modules.availabilities.repositories.AvailabilityRepository;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.modules.users.repositories.UserRepository;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;
    private final AvailabilityMapper availabilityMapper;

    public AvailabilityServiceImpl(AvailabilityRepository availabilityRepository,
            UserRepository userRepository,
            AvailabilityMapper availabilityMapper) {
        this.availabilityRepository = availabilityRepository;
        this.userRepository = userRepository;
        this.availabilityMapper = availabilityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDto> findByProgrammerId(Long programmerId) {
        return availabilityRepository.findByProgrammerId(programmerId)
                .stream()
                .map(availabilityMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDto> findActiveByProgrammerId(Long programmerId) {
        return availabilityRepository.findByProgrammerIdAndIsActiveTrue(programmerId)
                .stream()
                .map(availabilityMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AvailabilityResponseDto create(AvailabilityRequestDto availabilityDto) {
        UserEntity programmer = userRepository.findById(availabilityDto.getProgrammerId())
                .orElseThrow(() -> new NotFoundException(
                        "Programador no encontrado con ID: " + availabilityDto.getProgrammerId()));

        AvailabilityEntity availability = availabilityMapper.toEntity(availabilityDto);
        availability.setProgrammer(programmer);
        availability.setIsActive(true);

        AvailabilityEntity saved = availabilityRepository.save(availability);
        return availabilityMapper.toDto(saved);
    }

    @Override
    @Transactional
    public AvailabilityResponseDto update(Long id, AvailabilityRequestDto availabilityDto) {
        AvailabilityEntity availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Disponibilidad no encontrada con ID: " + id));

        availabilityMapper.updateEntity(availabilityDto, availability);
        AvailabilityEntity updated = availabilityRepository.save(availability);
        return availabilityMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!availabilityRepository.existsById(id)) {
            throw new NotFoundException("Disponibilidad no encontrada con ID: " + id);
        }
        availabilityRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AvailabilityResponseDto toggleStatus(Long id) {
        AvailabilityEntity availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Disponibilidad no encontrada con ID: " + id));

        availability.setIsActive(!availability.getIsActive());
        AvailabilityEntity updated = availabilityRepository.save(availability);
        return availabilityMapper.toDto(updated);
    }
}