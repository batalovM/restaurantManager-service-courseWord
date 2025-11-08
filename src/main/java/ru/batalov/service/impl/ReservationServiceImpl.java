package ru.batalov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.batalov.entity.Reservation;
import ru.batalov.entity.RestaurantTable;
import ru.batalov.entity.Visitor;
import ru.batalov.enums.ReservationStatus;
import ru.batalov.exception.ConstraintException;
import ru.batalov.exception.EntityNotExistException;
import ru.batalov.mapper.ReservationMapper;
import ru.batalov.mapper.RestaurantTableMapper;
import ru.batalov.repository.ReservationRepository;
import ru.batalov.repository.RestaurantTableRepository;
import ru.batalov.repository.VisitorRepository;
import ru.batalov.service.ReservationService;
import ru.batalov.views.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author batal
 * @Date 09.11.2025
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final static String NOT_FOUND_MESSAGE = "Reservation with id '%s' not found";

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final VisitorRepository visitorRepository;
    private final ReservationMapper reservationMapper;
    private final RestaurantTableMapper restaurantTableMapper;

    @Override
    public ReservationView create(ReservationDto dto) {
        try {
            RestaurantTable table = restaurantTableRepository.findById(dto.getVisitorId())
                    .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, "Table not found"));
            Reservation reservation = reservationMapper.mapDtoToEntity(dto);
            reservation.setRestaurantTable(table);
            if (dto.getVisitorId() != null) {
                Visitor visitor = visitorRepository.findById(dto.getVisitorId())
                        .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, "Visitor not found"));
                reservation.setVisitor(visitor);
            }
            reservation = reservationRepository.save(reservation);
            log.info("Create reservation {}", reservation);
            return reservationMapper.mapEntityToView(reservation);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public ReservationView getReservationById(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        return reservationMapper.mapEntityToView(reservation);
    }

    @Override
    public ReservationView update(UUID id, UpdateReservationDto dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        reservationMapper.updateEntityFromDto(dto, reservation);
        try {
            reservation = reservationRepository.save(reservation);
            log.info("Reservation {} updated", reservation);
            return reservationMapper.mapEntityToView(reservation);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void cancel(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        log.info("Reservation {} cancelled", id);
    }

    @Override
    public List<ReservationView> getReservationsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Reservation> reservations = reservationRepository.findByReservationDateTimeBetween(startOfDay, endOfDay);
        return reservationMapper.mapEntitiesToViews(reservations);
    }
    @Override
    public List<ReservationView> getReservationsByStatus(ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByStatus(status);
        return reservationMapper.mapEntitiesToViews(reservations);
    }
    @Override
    public List<ReservationView> getReservationsByTable(UUID tableId) {
        List<Reservation> reservations = reservationRepository.findByRestaurantTableId(tableId);
        return reservationMapper.mapEntitiesToViews(reservations);
    }
    @Override
    public List<ReservationView> getReservationsByVisitor(UUID visitorId) {
        List<Reservation> reservations = reservationRepository.findByVisitorId(visitorId);
        return reservationMapper.mapEntitiesToViews(reservations);
    }
    @Override
    public ReservationView confirmReservation(UUID id) {
        return changeReservationStatus(id, ReservationStatus.CONFIRMED);
    }
    @Override
    public ReservationView markAsSeated(UUID id) {
        return changeReservationStatus(id, ReservationStatus.SEATED);
    }
    @Override
    public ReservationView markAsCompleted(UUID id) {
        return changeReservationStatus(id, ReservationStatus.COMPLETED);
    }
    @Override
    public ReservationView markAsNoShow(UUID id) {
        return changeReservationStatus(id, ReservationStatus.NO_SHOW);
    }
    private ReservationView changeReservationStatus(UUID id, ReservationStatus newStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        reservation.setStatus(newStatus);
        Reservation saved = reservationRepository.save(reservation);
        log.info("Reservation {} status changed to {}", id, newStatus);
        return reservationMapper.mapEntityToView(saved);
    }
    @Override
    public List<ReservationSummaryView> getUpcomingReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<ReservationStatus> upcomingStatuses = Arrays.asList(
                ReservationStatus.PENDING, ReservationStatus.CONFIRMED
        );
        List<Reservation> reservations = reservationRepository.findByStatusInAndReservationDateTimeAfterOrderByReservationDateTime(
                upcomingStatuses, now);
        return reservationMapper.mapEntitiesToSummaryViews(reservations);
    }
    @Override
    public List<AvailableTableView> findAvailableTables(TableAvailabilityRequest request) {
        LocalDateTime startTime = request.getDateTime();
        LocalDateTime endTime = startTime.plusMinutes(request.getDurationMinutes());
        List<RestaurantTable> availableTables = reservationRepository
                .findAvailableTablesWithoutConflicts(request.getPartySize(), startTime, endTime);
        return availableTables.stream()
                .map(restaurantTableMapper::mapToAvailableView)
                .collect(Collectors.toList());
    }
}