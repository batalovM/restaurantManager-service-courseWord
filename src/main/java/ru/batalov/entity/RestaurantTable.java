package ru.batalov.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.batalov.enums.TableStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Entity
@Builder(setterPrefix = "with")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Integer tableNum = 0;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Column(columnDefinition = "TEXT")
    private String descriptions = "";

    @Column(nullable = false)
    private String zone = "";

    private TableStatus status = TableStatus.AVAILABLE;

    @OneToMany(mappedBy = "restaurantTable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<Reservation> reservations = new ArrayList<>();
}
