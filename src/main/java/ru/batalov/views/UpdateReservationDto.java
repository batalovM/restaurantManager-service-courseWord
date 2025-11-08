package ru.batalov.views;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Data
public class UpdateReservationDto {
    private LocalDateTime reservationDateTime;
    private Integer durationMin;
    private Integer visitorNum;
    private String specialRequests;
}
