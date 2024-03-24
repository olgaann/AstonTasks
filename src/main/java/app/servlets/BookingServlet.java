package app.servlets;

import app.db.DataBase;
import app.entities.Booking;
import app.repositories.BookingRepository;
import app.repositories.ClientRepository;
import app.services.BookingService;
import app.services.ClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class BookingServlet extends HttpServlet {
    private BookingService bookingService;

    @Override
    public void init() {
        DataBase dataBase = (DataBase) getServletContext().getAttribute("dataBase");

        BookingRepository bookingRepository = new BookingRepository(dataBase);
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
        //Валидный POST-запрос: /booking?client_id=n&room_id=m добавить новое бронирование
        PrintWriter writer = response.getWriter();

        String client_idString = request.getParameter("client_id");
        String room_idString = request.getParameter("room_id");

        Optional<Booking> newBooking = Optional.empty();

        try {
            long client_id = Long.parseLong(client_idString);
            long room_id = Long.parseLong(room_idString);
            newBooking = bookingService.add(client_id, room_id);
        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }
        if (newBooking.isPresent()) {
            writer.write("Booking added \n");
            List<Booking> bookingList = bookingService.findAll();
            writer.write("Bookings list: \n" + bookingService.bookingListToPrintView(bookingList));
        } else {
            writer.write("Booking has not been added.");
        }
    }




    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидные DELETE-запросы : /booking?client_id=n удаляет все бронирования клиента
        //Валидные DELETE-запросы : /booking?client_id=n&number=m учитывает номер комнаты

        PrintWriter writer = response.getWriter();
        String client_idString = request.getParameter("client_id");
        String numberString = request.getParameter("number");
        List<Booking> deletedList;
        try {
            long client_id = Long.parseLong(client_idString);
            if(numberString == null) {
                deletedList = bookingService.deleteAllByClientId(client_id);
            } else {
                int number = Integer.parseInt(numberString);
                deletedList = bookingService.deleteAllByClientIdAndNumber(client_id, number);
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
