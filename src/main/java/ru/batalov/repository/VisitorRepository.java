package ru.batalov.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.batalov.entity.Visitor;
import ru.batalov.views.VisitorStatistics;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID>, JpaSpecificationExecutor<Visitor> {
    @Query("SELECT v FROM Visitor v WHERE LOWER(v.firstName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Visitor> findByNamePartialMatch(@Param("name") String name);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Visitor> findByPhoneNumber(String phoneNumber);

    @Query("SELECT " +
            "COUNT(r) as totalReservations, " +
            "SUM(CASE WHEN r.status = ru.batalov.enums.ReservationStatus.COMPLETED THEN 1 ELSE 0 END) as completedReservations, " +
            "SUM(CASE WHEN r.status = ru.batalov.enums.ReservationStatus.CANCELLED THEN 1 ELSE 0 END) as cancelledReservations, " +
            "MAX(r.reservationDateTime) as lastVisitDate " +
            "FROM Reservation r " +
            "WHERE r.visitor.id = :visitorId")
    VisitorStatistics getVisitorStatistics(@Param("visitorId") UUID visitorId);
}
