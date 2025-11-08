package ru.batalov.service;

import ru.batalov.enums.ReservationStatus;
import ru.batalov.views.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
public interface ReservationService {
    ReservationView create(ReservationDto dto);
    ReservationView getReservationById(UUID id);
    ReservationView update(UUID id, UpdateReservationDto dto);
    void cancel(UUID id);

    List<ReservationView> getReservationsByDate(LocalDate date);
    List<ReservationView> getReservationsByStatus(ReservationStatus status);
    List<ReservationView> getReservationsByTable(UUID tableId);
    List<ReservationView> getReservationsByVisitor(UUID visitorId);

    ReservationView confirmReservation(UUID id);
    ReservationView markAsSeated(UUID id);
    ReservationView markAsCompleted(UUID id);
    ReservationView markAsNoShow(UUID id);

    List<ReservationSummaryView> getUpcomingReservations();
    List<AvailableTableView> findAvailableTables(TableAvailabilityRequest request);
}