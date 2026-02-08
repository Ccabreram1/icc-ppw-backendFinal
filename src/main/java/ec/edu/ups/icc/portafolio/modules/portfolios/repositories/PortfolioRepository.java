package ec.edu.ups.icc.portafolio.modules.portfolios.repositories;

import ec.edu.ups.icc.portafolio.modules.portfolios.models.PortfolioEntity;
import ec.edu.ups.icc.portafolio.modules.portfolios.models.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {
        Optional<PortfolioEntity> findByUserId(Long userId);

        boolean existsByUserId(Long userId);

        List<PortfolioEntity> findBySpeciality(Speciality speciality);

        Page<PortfolioEntity> findBySpeciality(Speciality speciality, Pageable pageable);

        List<PortfolioEntity> findByIsAvailableTrue();

        @Query("SELECT p FROM PortfolioEntity p WHERE p.user.name LIKE %:name%")
        Page<PortfolioEntity> findByUserNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

        Page<PortfolioEntity> findBySpecialityAndYearsExperienceBetween(
                        Speciality speciality, Integer minExperience, Integer maxExperience, Pageable pageable);

        Page<PortfolioEntity> findBySpecialityAndYearsExperienceGreaterThanEqual(
                        Speciality speciality, Integer minExperience, Pageable pageable);

        Page<PortfolioEntity> findBySpecialityAndYearsExperienceLessThanEqual(
                        Speciality speciality, Integer maxExperience, Pageable pageable);
}