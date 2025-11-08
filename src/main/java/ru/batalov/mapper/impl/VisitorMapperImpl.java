package ru.batalov.mapper.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.batalov.entity.Visitor;
import ru.batalov.exception.ServiceException;
import ru.batalov.mapper.VisitorMapper;
import ru.batalov.views.VisitorDto;
import ru.batalov.views.VisitorView;

import java.util.List;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Component
@Slf4j
public class VisitorMapperImpl implements VisitorMapper {
    @Override
    public Visitor mapDtoToEntity(VisitorDto unitDto) {
        return Visitor.builder()
                .withFirstName(unitDto.getFirstName())
                .withPhoneNumber(unitDto.getPhoneNumber())
                .build();
    }

    @Override
    public VisitorView mapEntityToView(Visitor unit){
        return VisitorView.builder()
                .withId(unit.getId())
                .withFirstName(unit.getFirstName())
                .withPhoneNumber(unit.getPhoneNumber())
                .build();
    }

    @Override
    public Page<VisitorView> mapEntitiesToViews(Page<Visitor> visitorPage){
        return visitorPage.map(this::mapEntityToView);
    }
}
