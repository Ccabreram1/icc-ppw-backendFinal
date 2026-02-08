package ec.edu.ups.icc.portafolio.modules.appointments.repositories;

import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentEntity;
import ec.edu.ups.icc.portafolio.modules.appointments.models.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
        List<AppointmentEntity> findByProgrammerId(Long programmerId);

        Page<AppointmentEntity> findByProgrammerId(Long programmerId, Pageable pageable);

        List<AppointmentEntity> findByClientId(Long clientId);

        Page<AppointmentEntity> findByClientId(Long clientId, Pageable pageable);

        List<AppointmentEntity> findByStatus(AppointmentStatus status);

        Page<AppointmentEntity> findByStatus(AppointmentStatus status, Pageable pageable);

        List<AppointmentEntity> findByDateTimeAfterAndStatusIn(
                        LocalDateTime dateTime, List<AppointmentStatus> statuses);

        boolean existsByProgrammerIdAndDateTimeBetween(
                        Long programmerId, LocalDateTime start, LocalDateTime end);

        @Query("SELECT a FROM AppointmentEntity a WHERE " +
                        "(:programmerId IS NULL OR a.programmer.id = :programmerId) AND " +
                        "(:clientId IS NULL OR a.client.id = :clientId) AND " +
                        "(:status IS NULL OR a.status = :status) AND " +
                        "(:startDate IS NULL OR a.dateTime >= :startDate) AND " +
                        "(:endDate IS NULL OR a.dateTime <= :endDate)")
        Page<AppointmentEntity> search(
                        @Param("programmerId") Long programmerId,
                        @Param("clientId") Long clientId,
                        @Param("status") AppointmentStatus status,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        Pageable pageable);

        List<AppointmentEntity> findByDateTimeBetweenAndStatusIn(
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end,
                        @Param("statuses") List<AppointmentStatus> statuses);

}