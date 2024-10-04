package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {

    private final EntityManager em;
    private final BookingService bookingService;

    @Test
    public void checkSuccessCreateBooking() {
        BookingRequestDto requestDto = prepareBooking(2L);
        BookingDto created = bookingService.createBooking(2L, requestDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", created.getId()).getSingleResult();

        assertThat(booking.getId(), equalTo(created.getId()));
        assertThat(booking.getStartDate(), equalTo(requestDto.getStart()));
        assertThat(booking.getEndDate(), equalTo(requestDto.getEnd()));
        assertThat(booking.getItem(), notNullValue());
        assertThat(booking.getBooker(), notNullValue());
        assertThat(booking.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    public void checkSuccessApprovedBooking() {
        BookingRequestDto requestDto = prepareBooking(2L);
        BookingDto created = bookingService.createBooking(2L, requestDto);
        bookingService.approveBooking(1L, created.getId(), Boolean.TRUE);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", created.getId()).getSingleResult();

        assertThat(booking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    public void checkSuccessGetBooking() {
        BookingDto bookingDto = bookingService.getBooking(2L, 1L);

        assertThat(bookingDto.getId(), equalTo(1L));
        assertThat(bookingDto.getStart(), equalTo(LocalDateTime.parse("2024-09-01T00:00")));
        assertThat(bookingDto.getEnd(), equalTo(LocalDateTime.parse("2024-09-02T00:00")));
        assertThat(bookingDto.getStatus(), equalTo(Status.APPROVED));
        assertThat(bookingDto.getItem(), notNullValue());
        assertThat(bookingDto.getBooker(), notNullValue());
    }

    @Test
    public void checkSuccessGetBookingByBookerId() {
        List<BookingDto> bookingDtos = bookingService.getBookingsByBookerId(2L, Status.PAST.toString());

        assertThat(bookingDtos.size(), equalTo(1));
        assertThat(bookingDtos.getFirst().getId(), equalTo(1L));
    }

    @Test
    public void checkSuccessGetBookingByOwnerId() {
        List<BookingDto> bookingDtos = bookingService.getBookingsByOwnerId(1L, Status.PAST.toString());

        assertThat(bookingDtos.size(), equalTo(2));
        assertThat(bookingDtos.getFirst().getId(), equalTo(2L));
    }

    private BookingRequestDto prepareBooking(Long itemId) {
        return BookingRequestDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.now().plusSeconds(5))
                .end(LocalDateTime.now().plusSeconds(10))
                .build();
    }
}
