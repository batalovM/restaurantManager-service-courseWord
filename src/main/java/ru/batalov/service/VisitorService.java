package ru.batalov.service;

import org.springframework.data.domain.Page;
import ru.batalov.entity.Reservation;
import ru.batalov.entity.Visitor;
import ru.batalov.exception.ConstraintException;
import ru.batalov.exception.EntityNotExistException;
import ru.batalov.exception.ServiceException;
import ru.batalov.views.VisitorDto;
import ru.batalov.views.VisitorStatistics;
import ru.batalov.views.VisitorView;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
public interface VisitorService {
    VisitorView getVisitorById(UUID id) throws EntityNotExistException;

    //Page<VisitorView> getVisitors(int size, int number, String search, String sort) throws ServiceException;

    VisitorView create(VisitorDto visitorDto) throws ConstraintException, ServiceException;

    VisitorView update(UUID id, VisitorDto visitorDto) throws EntityNotExistException,
            ConstraintException, ServiceException;

    void remove(UUID id) throws EntityNotExistException, ServiceException;

    List<Visitor> findVisitorsByName(String name) throws ServiceException;

    boolean existsByPhone(String phoneNumber) throws ServiceException;

    Visitor findOrCreateVisitor(String phoneNumber, String firstName) throws EntityNotExistException, ServiceException;

    List<Reservation> getVisitorReservations(UUID visitorId) throws EntityNotExistException;

    List<Reservation> getActiveVisitorReservations(UUID visitorId) throws ServiceException;

    List<Reservation> getVisitorReservationsByPeriod(UUID visitorId, LocalDate start, LocalDate end) throws ServiceException;

    VisitorStatistics getVisitorStatistics(UUID visitorId) throws EntityNotExistException, ServiceException;



}
