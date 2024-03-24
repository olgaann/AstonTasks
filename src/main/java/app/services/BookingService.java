package app.services;

import app.entities.Booking;
import app.repositories.BookingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingService {
    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String bookingListToPrintView(List<Booking> bookingList) {
        return bookingList.stream()
                .map(booking -> String.format("%s %s %s", booking.getClientId(), booking.getClientName(), booking.getNumber()))
                .collect(Collectors.joining("\n"));

    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> deleteAllByClientId(long clientId) {
        return bookingRepository.deleteAllByClientId(clientId);
    }

    public List<Booking> deleteAllByClientIdAndNumber(long clientId, int number) {
        return bookingRepository.deleteAllByClientIdAndNumber(clientId, number);
    }

    public Optional<Booking> add(long clientId, long roomId) {
        return bookingRepository.add(clientId, roomId);
    }
}
