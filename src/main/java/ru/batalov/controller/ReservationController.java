package ru.batalov.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.batalov.enums.ReservationStatus;
import ru.batalov.service.ReservationService;
import ru.batalov.views.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 09.11.2025
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Создать бронирование", description = "Создание нового бронирования столика")
    public ResponseEntity<ReservationView> createReservation(@RequestBody @Valid ReservationDto reservationDto) {
        ReservationView reservation = reservationService.create(reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping
    @Operation(summary = "Получить бронирование по ID", description = "Получение деталей бронирования ")
    public ResponseEntity<ReservationView> getReservation(@RequestParam UUID id) {
        ReservationView reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PutMapping
    @Operation(summary = "Обновить бронирование", description = "Обновление информации о существующем бронировании")
    public ResponseEntity<ReservationView> updateReservation(@RequestParam UUID id, @RequestBody @Valid UpdateReservationDto updateDto) {
        ReservationView reservation = reservationService.update(id, updateDto);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping
    @Operation(summary = "Отменить бронирование", description = "Отмена существующего бронирования")
    public ResponseEntity<Void> cancelReservation(@RequestParam UUID id) {
        reservationService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-date")
    @Operation(summary = "Получить бронирования по дате", description = "Получение всех бронирований на конкретную дату")
    public ResponseEntity<List<ReservationView>> getReservationsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ReservationView> reservations = reservationService.getReservationsByDate(date);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/by-status")
    @Operation(summary = "Получить бронирования по статусу", description = "Получение всех бронирований с определенным статусом")
    public ResponseEntity<List<ReservationView>> getReservationsByStatus(@RequestParam ReservationStatus status) {
        List<ReservationView> reservations = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/by-table")
    @Operation(summary = "Получить бронирования по столику", description = "Получение всех бронирований для конкретного столика")
    public ResponseEntity<List<ReservationView>> getReservationsByTable(@RequestParam UUID tableId) {
        List<ReservationView> reservations = reservationService.getReservationsByTable(tableId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/by-visitor")
    @Operation(summary = "Получить бронирования по посетителю", description = "Получение всех бронирований для конкретного посетителя")
    public ResponseEntity<List<ReservationView>> getReservationsByVisitor(@RequestParam UUID visitorId) {
        List<ReservationView> reservations = reservationService.getReservationsByVisitor(visitorId);
        return ResponseEntity.ok(reservations);
    }

    @PatchMapping("/confirm")
    @Operation(summary = "Подтвердить бронирование", description = "Изменение статуса бронирования на ПОДТВЕРЖДЕНО")
    public ResponseEntity<ReservationView> confirmReservation(@RequestParam UUID id) {
        ReservationView reservation = reservationService.confirmReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/seat")
    @Operation(summary = "Отметить как размещён", description = "Изменение статуса бронирования на РАЗМЕЩЕН когда гости прибыли")
    public ResponseEntity<ReservationView> markAsSeated(@RequestParam UUID id) {
        ReservationView reservation = reservationService.markAsSeated(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/complete")
    @Operation(summary = "Отметить как завершено", description = "Изменение статуса бронирования на ЗАВЕРШЕНО после ухода гостей")
    public ResponseEntity<ReservationView> markAsCompleted(@RequestParam UUID id) {
        ReservationView reservation = reservationService.markAsCompleted(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/no-show")
    @Operation(summary = "Отметить как неявку", description = "Изменение статуса бронирования на НЕЯВКА если гости не пришли")
    public ResponseEntity<ReservationView> markAsNoShow(@RequestParam UUID id) {
        ReservationView reservation = reservationService.markAsNoShow(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Получить предстоящие бронирования", description = "Получение списка предстоящих бронирований")
    public ResponseEntity<List<ReservationSummaryView>> getUpcomingReservations() {
        List<ReservationSummaryView> reservations = reservationService.getUpcomingReservations();
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/available-tables")
    @Operation(summary = "Найти доступные столики", description = "Поиск доступных столиков на указанное время и количество гостей")
    public ResponseEntity<List<AvailableTableView>> findAvailableTables(@RequestBody @Valid TableAvailabilityRequest request) {
        List<AvailableTableView> availableTables = reservationService.findAvailableTables(request);
        return ResponseEntity.ok(availableTables);
    }
}
