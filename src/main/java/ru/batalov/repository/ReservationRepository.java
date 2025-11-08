package ru.batalov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.batalov.entity.Reservation;
import ru.batalov.entity.RestaurantTable;
import ru.batalov.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByVisitorIdOrderByReservationDateTimeDesc(UUID customerId);
    List<Reservation> findByVisitorIdAndStatusInOrderByReservationDateTimeDesc(UUID customerId, List<ReservationStatus> statuses);
    List<Reservation> findByVisitorIdAndReservationDateTimeBetweenOrderByReservationDateTimeDesc(UUID customerId, LocalDateTime start, LocalDateTime end);

    List<Reservation> findByReservationDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByRestaurantTableId(UUID tableId);
    List<Reservation> findByVisitorId(UUID visitorId);
    List<Reservation> findByStatusInAndReservationDateTimeAfterOrderByReservationDateTime(List<ReservationStatus> statuses, LocalDateTime dateTime);

    @Query("SELECT t FROM RestaurantTable t WHERE " +
            "t.status = ru.batalov.enums.TableStatus.AVAILABLE AND " +
            "t.capacity >= :partySize AND " +
            "NOT EXISTS (SELECT r FROM Reservation r WHERE " +
            "r.restaurantTable = t AND " +
            "r.status IN (ru.batalov.enums.ReservationStatus.PENDING, " +
            "ru.batalov.enums.ReservationStatus.CONFIRMED, " +
            "ru.batalov.enums.ReservationStatus.SEATED) AND " +
            "r.reservationDateTime < :endTime AND " +
            "r.reservationDateTime >= :startTime)")
    List<RestaurantTable> findAvailableTablesWithoutConflicts(
            @Param("partySize") Integer partySize,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
