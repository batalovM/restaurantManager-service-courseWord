package ru.batalov.mapper.impl;

import org.springframework.stereotype.Component;
import ru.batalov.entity.Reservation;
import ru.batalov.mapper.ReservationMapper;
import ru.batalov.views.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public Reservation mapDtoToEntity(ReservationDto dto) {
        return Reservation.builder()
                .withReservationDateTime(dto.getReservationDateTime())
                .withDurationMin(dto.getDurationMin())
                .withVisitorNum(dto.getVisitorNum())
                .withCustomerName(dto.getCustomerName())
                .withCustomerPhone(dto.getCustomerPhone())
                .withSpecialRequests(dto.getSpecialRequests())
                .build();
    }

    @Override
    public ReservationView mapEntityToView(Reservation entity) {
        return ReservationView.builder()
                .withId(entity.getId())
                .withReservationDateTime(entity.getReservationDateTime())
                .withDurationMin(entity.getDurationMin())
                .withVisitorNum(entity.getVisitorNum())
                .withCustomerName(entity.getCustomerName())
                .withCustomerPhone(entity.getCustomerPhone())
                .withSpecialRequests(entity.getSpecialRequests())
                .withStatus(entity.getStatus())
                .withCreatedAt(entity.getCreatedAt())
                .withUpdatedAt(entity.getUpdatedAt())
                .withTableId(entity.getRestaurantTable() != null ? entity.getRestaurantTable().getId() : null)
                .withTableNum(entity.getRestaurantTable() != null ? entity.getRestaurantTable().getTableNum() : null)
                .withCustomerId(entity.getVisitor() != null ? entity.getVisitor().getId() : null)
                .build();
    }

    @Override
    public List<ReservationView> mapEntitiesToViews(List<Reservation> entities) {
        return entities.stream()
                .map(this::mapEntityToView)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationSummaryView> mapEntitiesToSummaryViews(List<Reservation> entities) {
        return entities.stream()
                .map(entity -> ReservationSummaryView.builder()
                        .withId(entity.getId())
                        .withReservationDateTime(entity.getReservationDateTime())
                        .withCustomerName(entity.getCustomerName())
                        .withTableNum(entity.getRestaurantTable() != null ? entity.getRestaurantTable().getTableNum() : null)
                        .withStatus(entity.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void updateEntityFromDto(UpdateReservationDto dto, Reservation entity) {
        if (dto.getReservationDateTime() != null) {
            entity.setReservationDateTime(dto.getReservationDateTime());
        }
        if (dto.getDurationMin() != null) {
            entity.setDurationMin(dto.getDurationMin());
        }
        if (dto.getVisitorNum() != null) {
            entity.setVisitorNum(dto.getVisitorNum());
        }
        if (dto.getSpecialRequests() != null) {
            entity.setSpecialRequests(dto.getSpecialRequests());
        }
    }
}
