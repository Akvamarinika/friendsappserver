package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.dto.request.EventFilter;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventUpdateDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import com.akvamarin.friendsappserver.domain.mapper.event.EventCategoryListMapper;
import com.akvamarin.friendsappserver.domain.mapper.event.EventMapper;
import com.akvamarin.friendsappserver.repositories.EventCategoryRepository;
import com.akvamarin.friendsappserver.repositories.EventRepository;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.akvamarin.friendsappserver.utils.FilterHelper.*;

/**
 * Сервис для работы с
 * данными мероприятий, взаимодействует с EventRepository, UserRepository
 * и EventCategoryRepository.
 *
 * @see Event
 * @see EventRepository
 * @see UserRepository
 * @see EventCategoryRepository
 * @see EventMapper
 * @see EventCategoryListMapper
 * */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final EventCategoryRepository categoryRepository;

    private final EventCategoryListMapper eventCategoryListMapper;

    private final UserRepository userRepository;


    /**
     * Создает новое мероприятие.
     *
     * @param eventDTO - мероприятие DTO
     * @return Event - созданное мероприятие
     */
    @Transactional
    @Override
    public Event createNewEvent(@NotNull EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);

        EventCategory eventCategory = categoryRepository.findById(eventDTO.getEventCategoryId())
                .orElseThrow(EntityNotFoundException::new);

        User userOwner = userRepository.findById(eventDTO.getOwnerId())
                .orElseThrow(EntityNotFoundException::new);

        event.setEventCategory(eventCategory);
        event.setUser(userOwner);
        return eventRepository.save(event);
    }

    /**
     * Сохраняет список мероприятий.
     *
     * @param eventDTOs список мероприятий DTO
     * @return список сохраненных мероприятий
     */
    @Transactional
    @Override
    public List<Event> saveAll(List<EventDTO> eventDTOs) {
        List<Event> events = eventDTOs.stream()
                .map(eventMapper::toEntity)
                .collect(Collectors.toList());
        return eventRepository.saveAll(events);
    }

    /**
     * Возвращает список всех мероприятий.
     *
     * @return список мероприятий DTO
     */
    @Transactional
    @Override
    public List<ViewEventDTO> findAll() {
        List<ViewEventDTO> result = eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Method *** findAll *** : EventDTO list size = {}", result.size());
        return result;
    }

    /**
     * Возвращает мероприятие по его идентификатору.
     *
     * @param eventId - идентификатор мероприятия
     * @return мероприятие DTO для отображения на интерфейсе
     * @throws EntityNotFoundException если мероприятие не найдено
     */
    @Transactional
    @Override
    public ViewEventDTO findById(long eventId) {
        ViewEventDTO result = eventRepository.findById(eventId)
                .map(eventMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with ID %d not found", eventId)));
        log.info("Method *** findById *** : EventDTO = {} EventID = {}", result, eventId);
        return result;
    }

    /**
     * Возвращает мероприятие для отображения на интерфейсе (UI) и обновления, по его идентификатору.
     *
     * @param eventId - идентификатор мероприятия
     * @return мероприятие DTO для отображения на интерфейсе (UI) и обновления
     * @throws EntityNotFoundException если мероприятие не найдено
     */
    @Transactional
    @Override
    public ViewEventUpdateDTO findByIdForViewUpdate(long eventId) {
        List<EventCategory> categories = categoryRepository.findAll();
        categories.sort(Comparator.comparing(EventCategory::getName));

        ViewEventUpdateDTO result = eventRepository.findById(eventId)
                .map(eventMapper::toUpdateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with ID %d not found", eventId)));

        List<EventCategoryDTO> categoryDTOs = eventCategoryListMapper.toDTOList(categories);
        result.setEventCategories(categoryDTOs);
        log.info("Method *** findByIdForViewUpdate *** : EventDTO = {} EventID = {}", result, eventId);
        return result;
    }

    /**
     * Обновляет мероприятие.
     *
     * @param eventDTO - мероприятие для обновления
     * @return обновленное мероприятие
     * @throws EntityNotFoundException если мероприятие не найдено
     */
    @Override
    @Transactional
    public ViewEventDTO updateEvent(@NonNull EventDTO eventDTO) {
        Event event = eventRepository.findById(eventDTO.getId())
                .map(ev -> {
                    Long categoryId = eventDTO.getEventCategoryId();
                    EventCategory category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new EntityNotFoundException(String.format("Event category with ID %d not found", categoryId)));

                    ev.setEventCategory(category);

                    eventMapper.updateEntity(eventDTO, ev);
                    return eventRepository.save(ev);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with ID %d not found", eventDTO.getId())));
        return eventMapper.toDTO(event);
    }

    /**
     * Удаляет мероприятие по его идентификатору.
     *
     * @param eventId - идентификатор мероприятия
     * @return true, если мероприятие успешно удалено, false - в случае неудачи
     * @throws EntityNotFoundException если мероприятие не найдено
     */
    @Override
    @Transactional
    public boolean deleteById(long eventId) {
        boolean isDeletedEvent = eventRepository.findById(eventId)
                .map(event -> {
                    eventRepository.deleteById(event.getId());
                    return true;
                }).orElseThrow(EntityNotFoundException::new);
        log.info("Method *** deleteById *** : isDeletedEvent = {} with ID = {}", isDeletedEvent, eventId);
        return isDeletedEvent;
    }

    /**
     * Удаляет все мероприятия.
     */
    @Override
    @Transactional
    public void deleteAll() {
        eventRepository.deleteAll();
    }

    /**
     * Фильтрует и сортирует мероприятия согласно заданным критериям.
     *
     * @param eventFilter - объект фильтра мероприятий
     * @return отфильтрованный список мероприятий DTO
     */
    @Transactional
    @Override
    public List<ViewEventDTO> filterEvents(EventFilter eventFilter) {
        List<Event> filteredEvents = eventRepository.findAll().stream()
                .filter(event -> matchesCity(event, eventFilter.getCityId()))
                .filter(event -> matchesCategories(event, eventFilter.getCategoryIds()))
                .filter(event -> matchesUserOrganizer(event, eventFilter.isUserOrganizer(), eventFilter.getCurrentUserId()))
                .filter(event -> matchesPartner(event, eventFilter.getPartnerList()))
                .filter(event -> matchesDaysOfWeek(event, eventFilter.getDaysOfWeekList()))
                .filter(event -> matchesPeriodOfTime(event, eventFilter.getPeriodOfTimeList()))
                .sorted(getEventComparator(eventFilter.getSortingType(), eventFilter.getUserCoords()))
                .collect(Collectors.toList());
        log.info("Method *** filterEvents *** : size = {} ", filteredEvents.size());
        log.info("Method *** filterEvents *** : eventFilter = {} ", eventFilter);

        return filteredEvents.stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }
}
