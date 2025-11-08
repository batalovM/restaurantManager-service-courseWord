package ru.batalov.views;

import lombok.Builder;
import lombok.Data;
import ru.batalov.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Data
@Builder(setterPrefix = "with")
public class ReservationSummaryView {
    private UUID id;
    private LocalDateTime reservationDateTime;
    private String customerName;
    private Integer tableNum;
    private ReservationStatus status;
}
