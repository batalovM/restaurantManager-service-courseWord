package ru.batalov.views;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author batal
 * @Date 08.11.2025
 */
public interface VisitorStatistics {
        Long getTotalReservations();
        Long getCompletedReservations();
        Long getCancelledReservations();
        LocalDateTime getLastVisitDate();

        default Double getSuccessRate() {
            Long total = getTotalReservations();
            Long completed = getCompletedReservations();
            if (total == null || total == 0 || completed == null) return 0.0;
            return (double) completed / total * 100;
        }
    static VisitorStatistics empty() {
        return new VisitorStatistics() {
            @Override
            public Long getTotalReservations() { return 0L; }
            @Override
            public Long getCompletedReservations() { return 0L; }
            @Override
            public Long getCancelledReservations() { return 0L; }
            @Override
            public LocalDateTime getLastVisitDate() { return null; }
        };
    }
}
