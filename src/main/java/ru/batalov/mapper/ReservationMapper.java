package ru.batalov.mapper;

import ru.batalov.entity.Reservation;
import ru.batalov.views.ReservationDto;
import ru.batalov.views.ReservationSummaryView;
import ru.batalov.views.ReservationView;
import ru.batalov.views.UpdateReservationDto;

import java.util.List;

/**
 * @author batal
 * @Date 08.11.2025
 */

public interface ReservationMapper {
    Reservation mapDtoToEntity(ReservationDto dto);
    ReservationView mapEntityToView(Reservation entity);
    List<ReservationView> mapEntitiesToViews(List<Reservation> entities);
    List<ReservationSummaryView> mapEntitiesToSummaryViews(List<Reservation> entities);

    void updateEntityFromDto(UpdateReservationDto dto, Reservation entity);
}
