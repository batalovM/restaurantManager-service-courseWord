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
public class ReservationView {
    private UUID id;
    private LocalDateTime reservationDateTime;
    private Integer durationMin;
    private Integer visitorNum;
    private String customerName;
    private String customerPhone;
    private String specialRequests;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID tableId;
    private Integer tableNum;
    private UUID customerId;
}
