package ru.batalov.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.batalov.enums.TableStatus;
import ru.batalov.exception.ServiceException;
import ru.batalov.service.RestaurantTableService;
import ru.batalov.views.AvailableTableView;
import ru.batalov.views.RestaurantTableDto;
import ru.batalov.views.RestaurantTableView;
import ru.batalov.views.TableAvailabilityRequest;

import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 09.11.2025
 */
@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    @PostMapping
    @Operation(summary = "Создать столик", description = "Создание нового столика в ресторане")
    public ResponseEntity<RestaurantTableView> createTable(@RequestBody @Valid RestaurantTableDto tableDto) {
        RestaurantTableView table = restaurantTableService.create(tableDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(table);
    }

    @GetMapping
    @Operation(summary = "Получить столик по ID", description = "Получение деталей столика")
    public ResponseEntity<RestaurantTableView> getTable(@RequestParam UUID id) {
        RestaurantTableView table = restaurantTableService.getTableById(id);
        return ResponseEntity.ok(table);
    }


    @PutMapping
    @Operation(summary = "Обновить столик", description = "Обновление информации о существующем столике")
    public ResponseEntity<RestaurantTableView> updateTable(
            @RequestParam UUID id,
            @RequestBody @Valid RestaurantTableDto tableDto) throws ServiceException {
        RestaurantTableView table = restaurantTableService.update(id, tableDto);
        return ResponseEntity.ok(table);
    }

    @DeleteMapping
    @Operation(summary = "Удалить столик", description = "Удаление столика из системы")
    public ResponseEntity<Void> deleteTable(@RequestParam UUID id) {
        restaurantTableService.removeTable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-zone")
    @Operation(summary = "Получить столики по зоне", description = "Получение столиков по зоне ресторана")
    public ResponseEntity<List<RestaurantTableView>> getTablesByZone(@RequestParam String zone) {
        List<RestaurantTableView> tables = restaurantTableService.getTablesByZone(zone);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/by-status")
    @Operation(summary = "Получить столики по статусу", description = "Получение столиков по текущему статусу")
    public ResponseEntity<List<RestaurantTableView>> getTablesByStatus(@RequestParam TableStatus status) {
        List<RestaurantTableView> tables = restaurantTableService.getTablesByStatus(status);
        return ResponseEntity.ok(tables);
    }

    @PatchMapping("/status")
    @Operation(summary = "Изменить статус столика", description = "Изменение статуса столика (доступен, занят, неисправен и т.д.)")
    public ResponseEntity<RestaurantTableView> changeTableStatus(@RequestParam UUID tableId, @RequestParam TableStatus newStatus) {
        RestaurantTableView table = restaurantTableService.changeTableStatus(tableId, newStatus);
        return ResponseEntity.ok(table);
    }

    @PostMapping("/available")
    @Operation(summary = "Найти доступные столики", description = "Поиск доступных столиков на указанное время и количество гостей")
    public ResponseEntity<List<AvailableTableView>> getAvailableTables(@RequestBody @Valid TableAvailabilityRequest request) {
        List<AvailableTableView> availableTables = restaurantTableService.getAvailableTables(request);
        return ResponseEntity.ok(availableTables);
    }
}
