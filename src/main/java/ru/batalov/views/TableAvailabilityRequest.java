package ru.batalov.views;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Data
public class TableAvailabilityRequest {
    private LocalDateTime dateTime;
    private Integer partySize;
    private Integer durationMinutes;
}
