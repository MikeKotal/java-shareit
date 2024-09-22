package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.Instant;
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
    List<Booking> findBookingByItemIdAndDate(Long itemId, Instant from, Instant to);

    List<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long bookerId, Instant startDate, Instant endDate);

    List<Booking> findAllByBookerIdAndEndDateBeforeOrderByStartDateDesc(Long bookerId, Instant endDate);

    List<Booking> findAllByBookerIdAndStartDateAfterOrderByStartDateDesc(Long bookerId, Instant startDate);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDateDesc(Long bookerId, Status status);

    List<Booking> findAllByBookerIdOrderByStartDateDesc(Long bookerId);

    List<Booking> findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(Long ownerId, Instant startDate, Instant endDate);

    List<Booking> findAllByItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(Long ownerId, Instant endDate);

    List<Booking> findAllByItemOwnerIdAndStartDateAfterOrderByStartDateDesc(Long ownerId, Instant startDate);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDateDesc(Long ownerId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartDateDesc(Long ownerId);

    Booking findFirstByItemIdAndItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(Long itemId, Long ownerId, Instant endDate);

    Booking findFirstByItemIdAndItemOwnerIdAndStartDateAfterOrderByStartDateDesc(Long itemId, Long ownerId, Instant startDate);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndDateBefore(Long bookerId, Long itemId, Instant current);
}
