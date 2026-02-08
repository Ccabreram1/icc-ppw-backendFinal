package ec.edu.ups.icc.portafolio.modules.portfolios.services;

import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioRequestDto;
import ec.edu.ups.icc.portafolio.modules.portfolios.dtos.PortfolioResponseDto;
import ec.edu.ups.icc.portafolio.modules.portfolios.models.PortfolioEntity;
import ec.edu.ups.icc.portafolio.modules.portfolios.models.Speciality;
import org.springframework.stereotype.Component;

@Component
public class PortfolioMapper {

    public PortfolioResponseDto toDto(PortfolioEntity portfolio) {
        PortfolioResponseDto dto = new PortfolioResponseDto();
        dto.setId(portfolio.getId());
        dto.setUserId(portfolio.getUser().getId());
        dto.setUserName(portfolio.getUser().getName());
        dto.setUserEmail(portfolio.getUser().getEmail());
        dto.setSpeciality(portfolio.getSpeciality().name());
        dto.setYearsExperience(portfolio.getYearsExperience());
        dto.setHourlyRate(portfolio.getHourlyRate());
        dto.setIsAvailable(portfolio.getIsAvailable());
        dto.setCreatedAt(portfolio.getCreatedAt());
        dto.setUpdatedAt(portfolio.getUpdatedAt());
        return dto;
    }

    public PortfolioEntity toEntity(PortfolioRequestDto dto) {
        PortfolioEntity portfolio = new PortfolioEntity();
        portfolio.setSpeciality(Speciality.valueOf(dto.getSpeciality().toUpperCase()));
        portfolio.setYearsExperience(dto.getYearsExperience());
        portfolio.setHourlyRate(dto.getHourlyRate());
        portfolio.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);
        return portfolio;
    }

    public void updateEntity(PortfolioRequestDto dto, PortfolioEntity portfolio) {
        if (dto.getSpeciality() != null) {
            portfolio.setSpeciality(Speciality.valueOf(dto.getSpeciality().toUpperCase()));
        }
        if (dto.getYearsExperience() != null) {
            portfolio.setYearsExperience(dto.getYearsExperience());
        }
        if (dto.getHourlyRate() != null) {
            portfolio.setHourlyRate(dto.getHourlyRate());
        }
        if (dto.getIsAvailable() != null) {
            portfolio.setIsAvailable(dto.getIsAvailable());
        }
    }
}