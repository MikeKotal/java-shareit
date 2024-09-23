package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select bk
            from Booking as bk
            join bk.item as it
            where it.id = ?1
            and (bk.startDate between ?2 and ?3
            or bk.endDate between ?2 and ?3)
            """)
    List<Booking> findBookingByItemIdAndDate(Long itemId, LocalDateTime from, LocalDateTime to);

    List<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfter(Long bookerId, LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    List<Booking> findAllByBookerIdAndEndDateBefore(Long bookerId, LocalDateTime endDate, Sort sort);

    List<Booking> findAllByBookerIdAndStartDateAfter(Long bookerId, LocalDateTime startDate, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfter(Long ownerId, LocalDateTime startDate, LocalDateTime endDate, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndDateBefore(Long ownerId, LocalDateTime endDate, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartDateAfter(Long ownerId, LocalDateTime startDate, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, Status status, Sort sort);

    List<Booking> findAllByItemOwnerId(Long ownerId, Sort sort);

    Booking findFirstByItemIdAndItemOwnerIdAndEndDateBefore(Long itemId, Long ownerId, LocalDateTime endDate, Sort sort);

    Booking findFirstByItemIdAndItemOwnerIdAndStartDateAfter(Long itemId, Long ownerId, LocalDateTime startDate, Sort sort);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndDateBefore(Long bookerId, Long itemId, LocalDateTime current);
}
