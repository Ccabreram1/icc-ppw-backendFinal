package ec.edu.ups.icc.portafolio.modules.availabilities.repositories;

import ec.edu.ups.icc.portafolio.modules.availabilities.models.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {
    List<AvailabilityEntity> findByProgrammerId(Long programmerId);

    List<AvailabilityEntity> findByProgrammerIdAndIsActiveTrue(Long programmerId);

    List<AvailabilityEntity> findByProgrammerIdAndDayOfWeekAndIsActiveTrue(
            Long programmerId, String dayOfWeek);
}