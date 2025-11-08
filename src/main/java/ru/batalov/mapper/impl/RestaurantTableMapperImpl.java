package ru.batalov.mapper.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.batalov.entity.RestaurantTable;
import ru.batalov.mapper.RestaurantTableMapper;
import ru.batalov.views.AvailableTableView;
import ru.batalov.views.RestaurantTableDto;
import ru.batalov.views.RestaurantTableView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Component
public class RestaurantTableMapperImpl implements RestaurantTableMapper {
    @Override
    public RestaurantTable mapDtoToEntity(RestaurantTableDto dto) {
        return RestaurantTable.builder()
                .withDescriptions(dto.getDescriptions())
                .withZone(dto.getZone())
                .withTableNum(dto.getTableNum())
                .build();
    }

    @Override
    public RestaurantTableView mapEntityToView(RestaurantTable entity) {
        return RestaurantTableView.builder()
                .withId(entity.getId())
                .withTableNum(entity.getTableNum())
                .withDescriptions(entity.getDescriptions())
                .withZone(entity.getZone())
                .withStatus(entity.getStatus())
                .build();
    }

    @Override
    public Page<RestaurantTableView> mapEntitiesToViews(Page<RestaurantTable> entities) {
        return entities.map(this::mapEntityToView);
    }

    public List<RestaurantTableView> mapEntitiesToViews(List<RestaurantTable> entities) {
        return entities.stream()
                .map(this::mapEntityToView)
                .collect(Collectors.toList());
    }
    @Override
    public AvailableTableView mapToAvailableView(RestaurantTable entity) {
        return AvailableTableView.builder()
                .withId(entity.getId())
                .withTableNum(entity.getTableNum())
                .withZone(entity.getZone())
                .withCapacity(entity.getCapacity())
                .build();
    }
}
