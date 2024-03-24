package app.servlets;

import app.db.DataBase;
import app.entities.Room;
import app.repositories.ClientRepository;
import app.repositories.RoomRepository;
import app.services.ClientService;
import app.services.RoomService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoomServlet extends HttpServlet {
    private RoomService roomService;

    @Override
    public void init() {
        DataBase dataBase = (DataBase) getServletContext().getAttribute("dataBase");
        RoomRepository roomRepository= new RoomRepository(dataBase);
        roomService = new RoomService(roomRepository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный GET-запрос: /room показать весь номерной фонд
        PrintWriter writer = response.getWriter();
        List<Room> roomList = roomService.findAll();
        writer.write("Rooms list: \n" + roomService.roomListToPrintView(roomList));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный POST-запрос: /room/?number=n
        PrintWriter writer = response.getWriter();
        String numberString = request.getParameter("number");

        Optional<Room> newRoom = Optional.empty();

        try {
            int number = Integer.parseInt(numberString);
            newRoom = roomService.add(number);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }

        if (newRoom.isPresent()) {
            writer.write("Room added: " + newRoom.get().toString());
        } else {
            writer.write("Room has not been added.");
        }
    }
}
