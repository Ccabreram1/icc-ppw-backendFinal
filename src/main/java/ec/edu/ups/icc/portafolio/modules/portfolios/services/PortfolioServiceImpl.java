package ec.edu.ups.icc.portafolio.modules.portfolios.services;

import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioRequestDto;
import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioResponseDto;
import ec.edu.ups.icc.portafolio.modules.portfolios.models.PortfolioEntity;
import ec.edu.ups.icc.portafolio.modules.portfolios.models.Speciality;
import ec.edu.ups.icc.portafolio.modules.portfolios.repositories.PortfolioRepository;
import ec.edu.ups.icc.portafolio.modules.users.models.UserEntity;
import ec.edu.ups.icc.portafolio.modules.users.repositories.UserRepository;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.ConflictException;
import ec.edu.ups.icc.portafolio.shared.exceptions.domain.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final PortfolioMapper portfolioMapper;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository,
            UserRepository userRepository,
            PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.portfolioMapper = portfolioMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PortfolioResponseDto> findAll(Pageable pageable) {
        return portfolioRepository.findAll(pageable)
                .map(portfolioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioResponseDto findById(Long id) {
        return portfolioRepository.findById(id)
                .map(portfolioMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Portafolio no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioResponseDto findByUserId(Long userId) {
        return portfolioRepository.findByUserId(userId)
                .map(portfolioMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Portafolio no encontrado para el usuario ID: " + userId));
    }

    @Override
    @Transactional
    public PortfolioResponseDto create(PortfolioRequestDto portfolioDto) {
        UserEntity user = userRepository.findById(portfolioDto.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + portfolioDto.getUserId()));

        if (portfolioRepository.existsByUserId(portfolioDto.getUserId())) {
            throw new ConflictException("El usuario ya tiene un portafolio registrado");
        }

        PortfolioEntity portfolio = portfolioMapper.toEntity(portfolioDto);
        portfolio.setUser(user);
        portfolio.setIsAvailable(true);

        PortfolioEntity savedPortfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.toDto(savedPortfolio);
    }

    @Override
    @Transactional
    public PortfolioResponseDto update(Long id, PortfolioRequestDto portfolioDto) {
        PortfolioEntity portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Portafolio no encontrado con ID: " + id));

        portfolioMapper.updateEntity(portfolioDto, portfolio);
        PortfolioEntity updatedPortfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.toDto(updatedPortfolio);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!portfolioRepository.existsById(id)) {
            throw new NotFoundException("Portafolio no encontrado con ID: " + id);
        }
        portfolioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioResponseDto> findBySpeciality(String speciality) {
        Speciality specialityEnum = Speciality.valueOf(speciality.toUpperCase());
        return portfolioRepository.findBySpeciality(specialityEnum)
                .stream()
                .map(portfolioMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioResponseDto> findAvailablePortfolios() {
        return portfolioRepository.findByIsAvailableTrue()
                .stream()
                .map(portfolioMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PortfolioResponseDto> search(String name, String speciality,
            Integer minExperience, Integer maxExperience,
            Pageable pageable) {
        if (speciality != null) {
            Speciality specialityEnum = Speciality.valueOf(speciality.toUpperCase());
            if (minExperience != null && maxExperience != null) {
                return portfolioRepository.findBySpecialityAndYearsExperienceBetween(
                        specialityEnum, minExperience, maxExperience, pageable)
                        .map(portfolioMapper::toDto);
            } else if (minExperience != null) {
                return portfolioRepository.findBySpecialityAndYearsExperienceGreaterThanEqual(
                        specialityEnum, minExperience, pageable)
                        .map(portfolioMapper::toDto);
            } else if (maxExperience != null) {
                return portfolioRepository.findBySpecialityAndYearsExperienceLessThanEqual(
                        specialityEnum, maxExperience, pageable)
                        .map(portfolioMapper::toDto);
            } else {
                return portfolioRepository.findBySpeciality(specialityEnum, pageable)
                        .map(portfolioMapper::toDto);
            }
        }

        if (name != null) {
            return portfolioRepository.findByUserNameContainingIgnoreCase(name, pageable)
                    .map(portfolioMapper::toDto);
        }

        return portfolioRepository.findAll(pageable)
                .map(portfolioMapper::toDto);
    }
}