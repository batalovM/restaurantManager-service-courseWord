package ru.batalov.views;

import lombok.Data;
import ru.batalov.entity.Visitor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Data
public class ReservationDto {
    private LocalDateTime reservationDateTime;
    private Integer durationMin;
    private Integer visitorNum;
    private String customerName;
    private String customerPhone;
    private UUID visitorId;
    private String specialRequests;
}
