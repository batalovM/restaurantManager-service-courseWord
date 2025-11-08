package ru.batalov.service;

import org.springframework.data.domain.Page;
import ru.batalov.enums.TableStatus;
import ru.batalov.exception.EntityNotExistException;
import ru.batalov.exception.ServiceException;
import ru.batalov.views.*;

import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
public interface RestaurantTableService {
    RestaurantTableView create(RestaurantTableDto dto);
    RestaurantTableView getTableById(UUID id) throws EntityNotExistException;
    //Page<RestaurantTableView> getRestaurantsTables(int size, int number, String search, String sort) throws ServiceException;
    RestaurantTableView update(UUID id, RestaurantTableDto dto) throws ServiceException;
    void removeTable(UUID id);

    List<RestaurantTableView> getTablesByZone(String zone);
    List<RestaurantTableView> getTablesByStatus(TableStatus status);
    RestaurantTableView changeTableStatus(UUID tableId, TableStatus newStatus);

    List<AvailableTableView> getAvailableTables(TableAvailabilityRequest request);
}
