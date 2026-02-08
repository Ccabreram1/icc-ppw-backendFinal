package ec.edu.ups.icc.portafolio.modules.notifications.repositories;

import ec.edu.ups.icc.portafolio.modules.notifications.models.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserId(Long userId);

    Page<NotificationEntity> findByUserId(Long userId, Pageable pageable);

    List<NotificationEntity> findByUserIdAndReadFalse(Long userId);

    long countByUserIdAndReadFalse(Long userId);

    List<NotificationEntity> findByType(String type);

    List<NotificationEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<NotificationEntity> findByUserIdAndType(Long userId, String type);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.read = true, n.readAt = CURRENT_TIMESTAMP WHERE n.user.id = :userId AND n.read = false")
    int markAllAsReadByUserId(@Param("userId") Long userId);

    @Modifying
    void deleteByUserId(Long userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<NotificationEntity> findLatestByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT n.type, COUNT(n) FROM NotificationEntity n WHERE n.createdAt >= :since GROUP BY n.type")
    List<Object[]> countByTypeSince(@Param("since") LocalDateTime since);
}