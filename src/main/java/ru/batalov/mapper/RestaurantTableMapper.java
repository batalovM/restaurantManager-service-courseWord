package ru.batalov.mapper;

import org.springframework.data.domain.Page;
import ru.batalov.entity.RestaurantTable;
import ru.batalov.views.AvailableTableView;
import ru.batalov.views.RestaurantTableDto;
import ru.batalov.views.RestaurantTableView;

import java.util.List;

/**
 * @author batal
 * @Date 08.11.2025
 */

public interface RestaurantTableMapper {
    RestaurantTable mapDtoToEntity(RestaurantTableDto dto);
    RestaurantTableView mapEntityToView(RestaurantTable entity);
    Page<RestaurantTableView> mapEntitiesToViews(Page<RestaurantTable> entities);
    List<RestaurantTableView> mapEntitiesToViews(List<RestaurantTable> restaurantTables);
    AvailableTableView mapToAvailableView(RestaurantTable entity);
}
