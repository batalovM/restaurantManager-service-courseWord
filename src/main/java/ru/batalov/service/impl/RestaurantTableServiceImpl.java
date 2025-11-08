package ru.batalov.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.batalov.entity.RestaurantTable;
import ru.batalov.enums.TableStatus;
import ru.batalov.exception.ConstraintException;
import ru.batalov.exception.EntityNotExistException;
import ru.batalov.exception.ServiceException;
import ru.batalov.mapper.RestaurantTableMapper;
import ru.batalov.repository.ReservationRepository;
import ru.batalov.repository.RestaurantTableRepository;
import ru.batalov.service.RestaurantTableService;
import ru.batalov.views.AvailableTableView;
import ru.batalov.views.RestaurantTableDto;
import ru.batalov.views.RestaurantTableView;
import ru.batalov.views.TableAvailabilityRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author batal
 * @Date 09.11.2025
 */
@Service
@Slf4j
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final static String NOT_FOUND_MESSAGE = "Restaurant Table with id '%s' not found";

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantTableMapper restaurantTableMapper;;
    private final ReservationRepository reservationRepository;

    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository, RestaurantTableMapper restaurantTableMapper, ReservationRepository reservationRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.restaurantTableMapper = restaurantTableMapper;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public RestaurantTableView create(RestaurantTableDto dto) {
        try{
            RestaurantTable restaurantTable = restaurantTableMapper.mapDtoToEntity(dto);
            restaurantTable = restaurantTableRepository.saveAndFlush(restaurantTable);
            log.info("Create restaurantTable {}", restaurantTable);
            return restaurantTableMapper.mapEntityToView(restaurantTable);
        }catch (DataIntegrityViolationException e){
            throw new ConstraintException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public RestaurantTableView getTableById(UUID id) throws EntityNotExistException {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        return restaurantTableMapper.mapEntityToView(restaurantTable);
    }

//    @Override
//    public Page<RestaurantTableView> getRestaurantsTables(int size, int number, String search, String sort) throws ServiceException {
//        if(search.isEmpty() && sort.isEmpty()){
//            return restaurantTableMapper.mapEntitiesToViews(restaurantTableRepository.findAll(PageRequest.of(number, size)));
//        }
//        Specification<RestaurantTable> spec = searchRequestCreator.createSpec(search);
//        Pageable pageable = searchRequestCreator.createPageable(size, number, sort);
//        return restaurantTableMapper.mapEntitiesToViews(restaurantTableRepository.findAll(spec, pageable));
//    }

    @Override
    public RestaurantTableView update(UUID id, RestaurantTableDto dto) throws ServiceException {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        restaurantTable.setTableNum(dto.getTableNum() != null ? dto.getTableNum() : restaurantTable.getTableNum());
        restaurantTable.setDescriptions(dto.getDescriptions() != null ? dto.getDescriptions() : restaurantTable.getDescriptions());
        restaurantTable.setZone(dto.getZone() != null ? dto.getZone() : restaurantTable.getZone());
        try {
            restaurantTable = restaurantTableRepository.save(restaurantTable);
            log.info("Restaurant Table {} updated", restaurantTable);
            return restaurantTableMapper.mapEntityToView(restaurantTable);
        }catch (DataIntegrityViolationException e) {
            throw new ConstraintException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void removeTable(UUID id) {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        restaurantTableRepository.delete(restaurantTable);
    }

    @Override
    public List<RestaurantTableView> getTablesByZone(String zone) {
        List<RestaurantTable> tables = restaurantTableRepository.findByZone(zone);
        return restaurantTableMapper.mapEntitiesToViews(tables);
    }

    @Override
    public List<RestaurantTableView> getTablesByStatus(TableStatus status) {
        List<RestaurantTable> tables = restaurantTableRepository.findByStatus(status);
        return restaurantTableMapper.mapEntitiesToViews(tables);
    }

    @Override
    public RestaurantTableView changeTableStatus(UUID tableId, TableStatus newStatus) {
        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.NOT_FOUND, "Table not found"));
        table.setStatus(newStatus);
        RestaurantTable saved = restaurantTableRepository.save(table);
        return restaurantTableMapper.mapEntityToView(saved);
    }

    @Override
    public List<AvailableTableView> getAvailableTables(TableAvailabilityRequest request) {
        LocalDateTime startTime = request.getDateTime();
        LocalDateTime endTime = startTime.plusMinutes(request.getDurationMinutes());

        List<RestaurantTable> availableTables = reservationRepository
                .findAvailableTablesWithoutConflicts(request.getPartySize(), startTime, endTime);

        return availableTables.stream()
                .map(restaurantTableMapper::mapToAvailableView)
                .collect(Collectors.toList());
    }
}
