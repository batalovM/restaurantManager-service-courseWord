package ru.batalov.enums;

/**
 * @author batal
 * @Date 08.11.2025
 */
public enum ReservationStatus {
    PENDING,    // Ожидает подтверждения
    CONFIRMED,  // Подтверждено
    SEATED,     // Гости размещены за столиком
    COMPLETED,  // Завершено (гости ушли)
    CANCELLED,  // Отменено
    NO_SHOW     // Гости не явились
}
