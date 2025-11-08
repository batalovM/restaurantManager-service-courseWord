package ru.batalov.views;

import lombok.Data;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Data
public class RestaurantTableDto {
    private Integer tableNum;
    private String descriptions;
    private String zone;
}
