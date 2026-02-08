package ec.edu.ups.icc.portafolio.modules.portfolios.services;

import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioRequestDto;
import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PortfolioService {
    Page<PortfolioResponseDto> findAll(Pageable pageable);

    PortfolioResponseDto findById(Long id);

    PortfolioResponseDto findByUserId(Long userId);

    PortfolioResponseDto create(PortfolioRequestDto portfolioDto);

    PortfolioResponseDto update(Long id, PortfolioRequestDto portfolioDto);

    void delete(Long id);

    List<PortfolioResponseDto> findBySpeciality(String speciality);

    List<PortfolioResponseDto> findAvailablePortfolios();

    Page<PortfolioResponseDto> search(String name, String speciality,
            Integer minExperience, Integer maxExperience,
            Pageable pageable);
}