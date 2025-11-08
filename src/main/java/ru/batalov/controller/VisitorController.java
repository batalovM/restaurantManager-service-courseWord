package ru.batalov.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.batalov.entity.Reservation;
import ru.batalov.entity.Visitor;
import ru.batalov.exception.ServiceException;
import ru.batalov.service.VisitorService;
import ru.batalov.views.VisitorDto;
import ru.batalov.views.VisitorStatistics;
import ru.batalov.views.VisitorView;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 09.11.2025
 */
@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @GetMapping
    @Operation(summary = "Получить посетителя по ID", description = "Получение деталей посетителя")
    public ResponseEntity<VisitorView> getVisitor(@RequestParam UUID id) {
        VisitorView visitor = visitorService.getVisitorById(id);
        return ResponseEntity.ok(visitor);
    }


    @PostMapping
    @Operation(summary = "Создать посетителя", description = "Создание нового посетителя в системе")
    public ResponseEntity<VisitorView> createVisitor(@RequestBody @Valid VisitorDto visitorDto) throws ServiceException {
        VisitorView visitor = visitorService.create(visitorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(visitor);
    }

    @PutMapping
    @Operation(summary = "Обновить посетителя", description = "Обновление информации о существующем посетителе")
    public ResponseEntity<VisitorView> updateVisitor(
            @RequestParam UUID id,
            @RequestBody @Valid VisitorDto visitorDto) throws ServiceException {
        VisitorView visitor = visitorService.update(id, visitorDto);
        return ResponseEntity.ok(visitor);
    }

    @DeleteMapping
    @Operation(summary = "Удалить посетителя", description = "Удаление посетителя из системы")
    public ResponseEntity<Void> deleteVisitor(@RequestParam UUID id) throws ServiceException {
        visitorService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search-by-name")
    @Operation(summary = "Найти посетителей по имени", description = "Поиск посетителей по частичному совпадению имени")
    public ResponseEntity<List<Visitor>> findVisitorsByName(@RequestParam String name) throws ServiceException {
        List<Visitor> visitors = visitorService.findVisitorsByName(name);
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/check-phone")
    @Operation(summary = "Проверить существование телефона", description = "Проверка существования посетителя с указанным номером телефона")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phoneNumber) throws ServiceException {
        boolean exists = visitorService.existsByPhone(phoneNumber);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/find-or-create")
    @Operation(summary = "Найти или создать посетителя", description = "Поиск посетителя по номеру телефона или создание нового")
    public ResponseEntity<Visitor> findOrCreateVisitor(
            @RequestParam String phoneNumber,
            @RequestParam String firstName) throws ServiceException {
        Visitor visitor = visitorService.findOrCreateVisitor(phoneNumber, firstName);
        return ResponseEntity.ok(visitor);
    }

    @GetMapping("/reservations")
    @Operation(summary = "Получить бронирования посетителя", description = "Получение всех бронирований конкретного посетителя")
    public ResponseEntity<List<Reservation>> getVisitorReservations(@RequestParam UUID visitorId) {
        List<Reservation> reservations = visitorService.getVisitorReservations(visitorId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/active-reservations")
    @Operation(summary = "Получить активные бронирования", description = "Получение активных бронирований посетителя (ожидающие, подтвержденные, размещенные)")
    public ResponseEntity<List<Reservation>> getActiveVisitorReservations(@RequestParam UUID visitorId) throws ServiceException {
        List<Reservation> reservations = visitorService.getActiveVisitorReservations(visitorId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/reservations-by-period")
    @Operation(summary = "Получить бронирования за период", description = "Получение бронирований посетителя за указанный период")
    public ResponseEntity<List<Reservation>> getVisitorReservationsByPeriod(
            @RequestParam UUID visitorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) throws ServiceException {
        List<Reservation> reservations = visitorService.getVisitorReservationsByPeriod(visitorId, start, end);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Получить статистику посетителя", description = "Получение статистики по бронированиям посетителя")
    public ResponseEntity<VisitorStatistics> getVisitorStatistics(@RequestParam UUID visitorId) throws ServiceException {
        VisitorStatistics statistics = visitorService.getVisitorStatistics(visitorId);
        return ResponseEntity.ok(statistics);
    }
}
