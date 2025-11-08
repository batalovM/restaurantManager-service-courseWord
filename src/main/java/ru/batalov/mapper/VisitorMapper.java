package ru.batalov.mapper;

import org.springframework.data.domain.Page;
import ru.batalov.entity.Visitor;
import ru.batalov.exception.ServiceException;
import ru.batalov.views.*;

/**
 * @author batal
 * @Date 08.11.2025
 */
public interface VisitorMapper {
    Visitor mapDtoToEntity(VisitorDto unitDto);

    VisitorView mapEntityToView(Visitor unit);
    Page<VisitorView> mapEntitiesToViews(Page<Visitor> pageUnits);
}
