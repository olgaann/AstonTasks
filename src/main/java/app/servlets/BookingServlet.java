package app.servlets;

import app.db.DataBase;
import app.entities.Booking;
import app.repositories.BookingRepository;
import app.repositories.ClientRepository;
import app.services.BookingService;
import app.services.ClientService;
import org.hibernate.SessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingServlet extends HttpServlet {
    private BookingService bookingService;

    @Override
    public void init() {
        DataBase dataBase = (DataBase) getServletContext().getAttribute("dataBase");
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        BookingRepository bookingRepository = new BookingRepository(dataBase, sessionFactory);
        bookingService = new BookingService(bookingRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный GET-запрос: /booking показать все бронирования всех клиентов
        PrintWriter writer = response.getWriter();
        List<Booking> bookingList = bookingService.findAll();

        writer.write("Bookings list: \n" + bookingService.bookingListToPrintView(bookingList));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный POST-запрос: /booking?clientId=n&roomId=m добавить новое бронирование
        PrintWriter writer = response.getWriter();

        String clientIdString = request.getParameter("clientId");
        String roomIdString = request.getParameter("roomId");

        Optional<Booking> newBooking = Optional.empty();

        try {
            long clientId = Long.parseLong(clientIdString);
            long roomId = Long.parseLong(roomIdString);
            newBooking = bookingService.add(clientId, roomId);
        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }
        if (newBooking.isPresent()) {
            writer.write("Booking added \n");

            writer.write(newBooking.get().toString());
        } else {
            writer.write("Booking has not been added.");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидные DELETE-запросы : /booking?clientId=n удаляет все бронирования клиента
        //Валидные DELETE-запросы : /booking?clientId=n&number=m учитывает номер комнаты

        PrintWriter writer = response.getWriter();
        String clientIdString = request.getParameter("clientId");
        String numberString = request.getParameter("number");
        List<Booking> deletedList = new ArrayList<>();
        try {
            long clientId = Long.parseLong(clientIdString);
            if (numberString == null) {
                deletedList = bookingService.deleteAllByClientId(clientId);
            } else {
                int number = Integer.parseInt(numberString);
                deletedList = bookingService.deleteAllByClientIdAndNumber(clientId, number);
            }
        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }

        if (deletedList.isEmpty()) {
            writer.write("No bookings deleted");
            return;
        }
        writer.write("Deleted bookings: \n" + bookingService.bookingListToPrintView(deletedList));

    }
}
