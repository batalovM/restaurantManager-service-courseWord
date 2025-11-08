package ru.batalov.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.batalov.entity.Reservation;
import ru.batalov.entity.Visitor;
import ru.batalov.enums.ReservationStatus;
import ru.batalov.exception.ConstraintException;
import ru.batalov.exception.EntityNotExistException;
import ru.batalov.exception.ServiceException;
import ru.batalov.mapper.VisitorMapper;
import ru.batalov.repository.ReservationRepository;
import ru.batalov.repository.VisitorRepository;
import ru.batalov.service.VisitorService;
import ru.batalov.views.VisitorDto;
import ru.batalov.views.VisitorStatistics;
import ru.batalov.views.VisitorView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author batal
 * @Date 08.11.2025
 */
@Service
@Slf4j
public class VisitorServiceImpl implements VisitorService {

    private final static String NOT_FOUND_MESSAGE = "Visitor with id '%s' not found";

    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;
    private final ReservationRepository reservationRepository;
    public VisitorServiceImpl(VisitorRepository visitorRepository, VisitorMapper visitorMapper, ReservationRepository reservationRepository) {
        this.visitorRepository = visitorRepository;
        this.visitorMapper = visitorMapper;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public VisitorView getVisitorById(UUID id) throws EntityNotExistException {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        return visitorMapper.mapEntityToView(visitor);
    }

//    @Override
//    public Page<VisitorView> getVisitors(int size, int number, String search, String sort) throws ServiceException {
//        if(search.isEmpty() && sort.isEmpty()){
//            return visitorMapper.mapEntitiesToViews(visitorRepository.findAll(PageRequest.of(number, size)));
//        }
//        Specification<Visitor> spec = searchRequestCreator.createSpec(search);
//        Pageable pageable = searchRequestCreator.createPageable(size, number, sort);
//        return visitorMapper.mapEntitiesToViews(visitorRepository.findAll(spec, pageable));
//    }

    @Override
    @Transactional
    public VisitorView create(VisitorDto visitorDto) throws ConstraintException, ServiceException {
        try{
            Visitor visitor = visitorMapper.mapDtoToEntity(visitorDto);
            visitor = visitorRepository.saveAndFlush(visitor);
            log.info("Create visitor {}", visitor);
            return visitorMapper.mapEntityToView(visitor);
        }catch (DataIntegrityViolationException e){
            throw new ConstraintException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public VisitorView update(UUID id, VisitorDto visitorDto) throws EntityNotExistException, ConstraintException, ServiceException {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        visitor.setFirstName(visitorDto.getFirstName() != null ? visitorDto.getFirstName() : visitor.getFirstName());
        visitor.setPhoneNumber(visitorDto.getPhoneNumber() != null ? visitorDto.getPhoneNumber() : visitor.getPhoneNumber());
        try{
            visitor = visitorRepository.save(visitor);
            log.info("Visitor {} updated", visitor);
            return visitorMapper.mapEntityToView(visitor);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    @Transactional
    public void remove(UUID id) throws EntityNotExistException, ServiceException {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, id));
        visitorRepository.delete(visitor);
    }

    @Override
    @Transactional
    public List<Visitor> findVisitorsByName(String name) throws ServiceException {
        try {
            return visitorRepository.findByNamePartialMatch(name);
        }catch (Exception e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Visitor not found with name: ", name);
        }
    }

    @Override
    public boolean existsByPhone(String phoneNumber) throws ServiceException {
        try {
            return visitorRepository.existsByPhoneNumber(phoneNumber);
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Error checking phone existence: " + phoneNumber, e);
        }
    }

    @Override
    @Transactional
    public Visitor findOrCreateVisitor(String phoneNumber, String firstName) throws ServiceException {
        try {
            return visitorRepository.findByPhoneNumber(phoneNumber)
                    .orElseGet(() -> {
                        Visitor newVisitor = new Visitor();
                        newVisitor.setPhoneNumber(phoneNumber);
                        newVisitor.setFirstName(firstName);
                        return visitorRepository.save(newVisitor);
                    });
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Error finding or creating visitor ", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getVisitorReservations(UUID visitorId) throws EntityNotExistException {
        if (!visitorRepository.existsById(visitorId)) {
            throw new EntityNotExistException(HttpStatus.BAD_REQUEST, NOT_FOUND_MESSAGE, visitorId);
        }
        return reservationRepository.findByVisitorIdOrderByReservationDateTimeDesc(visitorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getActiveVisitorReservations(UUID visitorId) throws ServiceException {
        try {
            List<ReservationStatus> activeStatuses = Arrays.asList(
                    ReservationStatus.PENDING,
                    ReservationStatus.CONFIRMED,
                    ReservationStatus.SEATED
            );
            return reservationRepository.findByVisitorIdAndStatusInOrderByReservationDateTimeDesc(
                    visitorId, activeStatuses);
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Error getting active reservations for visitor: " + visitorId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getVisitorReservationsByPeriod(UUID visitorId, LocalDate start, LocalDate end) throws ServiceException {
        try {
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
            return reservationRepository.findByVisitorIdAndReservationDateTimeBetweenOrderByReservationDateTimeDesc(
                    visitorId, startDateTime, endDateTime);
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Error getting reservations by period for visitor: " + visitorId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public VisitorStatistics getVisitorStatistics(UUID visitorId) throws ServiceException {
        try {
            if (!visitorRepository.existsById(visitorId)) {
                throw new EntityNotExistException(HttpStatus.NOT_FOUND, "Visitor not found with id: " + visitorId);
            }
            VisitorStatistics statistics = visitorRepository.getVisitorStatistics(visitorId);
            if (statistics == null || statistics.getTotalReservations() == null) {
                return VisitorStatistics.empty();
            }
            return statistics;
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Error getting statistics for visitor: " + visitorId, e);
        }
    }
}
