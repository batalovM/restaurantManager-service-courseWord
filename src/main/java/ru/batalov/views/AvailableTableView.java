package ru.batalov.views;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Data
@Builder(setterPrefix = "with")
public class AvailableTableView {
    private UUID id;
    private Integer tableNum;
    private String zone;
    private Integer capacity;
}
