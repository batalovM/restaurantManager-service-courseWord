package ru.batalov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.batalov.entity.RestaurantTable;
import ru.batalov.enums.TableStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author batal
 * @Date 09.11.2025
 */
public interface RestaurantTableRepository  extends JpaRepository<RestaurantTable, UUID>, JpaSpecificationExecutor<RestaurantTable> {
    List<RestaurantTable> findByZone(String zone);

    List<RestaurantTable> findByStatus(TableStatus status);
}
