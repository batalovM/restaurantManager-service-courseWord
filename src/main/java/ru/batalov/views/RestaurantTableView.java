package ru.batalov.views;

import lombok.Builder;
import lombok.Data;
import ru.batalov.enums.TableStatus;

import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Data
@Builder(setterPrefix = "with")
public class RestaurantTableView {
    private UUID id;
    private Integer tableNum;
    private String descriptions;
    private String zone;
    private TableStatus status;
}