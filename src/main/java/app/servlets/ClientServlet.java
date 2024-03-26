package app.servlets;

import app.db.DataBase;
import app.entities.Client;
import app.repositories.ClientRepository;
import app.services.ClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ClientServlet extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init() {
        DataBase dataBase = (DataBase) getServletContext().getAttribute("dataBase");

        ClientRepository clientRepository = new ClientRepository(dataBase);
        clientService = new ClientService(clientRepository);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Валидные GET-запросы : /client   /client/?id=n  (+ выводит список номеров, забронированных клиентом)  /client/?name=clientName

        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        String result = parseRequestURIAndQueryString(requestURI, queryString);
        PrintWriter writer = response.getWriter();
        writer.write("Clients list: \n" + result);
    }

    public String parseRequestURIAndQueryString(String requestURI, String queryString) {
        System.out.println(requestURI);
        if (requestURI.equals("/client") || requestURI.equals("/client/")) {
            if(queryString == null) {
                return clientService.getAllClients().toString();
            }
            return parseQueryString(queryString);

        }
        return "Your request is invalid";
    }

    public String parseQueryString (String queryString) {
        try {
            if(queryString.matches( "^id=\\d+$")) {
                long id = Long.parseLong(queryString.substring(queryString.indexOf("=") + 1));
                return clientService.getById(id).toString() + "\n" + clientService.getRoomsNumbersByClientId(id).toString();
            }
            if(queryString.matches("^name=[a-zA-Z]+$")) {
                String name = queryString.substring(queryString.indexOf("=") + 1);
                return clientService.getByName(name).toString();
            }
            return "Your request is invalid";
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "No such client found";
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный POST-запрос : /client?name=Name&phone=phone
        PrintWriter writer = response.getWriter();
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        Client newClient = null;
        try {
            newClient = clientService.add(name, phone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (newClient == null) {
            writer.write("Client has not added(");
        } else {
            writer.write("Client added: " + newClient.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный PUT-запрос : /client?id=n&name=newName&phone=newPhone
        PrintWriter writer = response.getWriter();
        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        System.out.println(name);
        System.out.println(phone);
        if (idString == null) {
            writer.write("Your request is invalid");
            return;
        }
        if (name == null && phone == null) {
            writer.write("Your request is invalid: no updated fields");
            return;
        }

        Client updatedClient = null;
        try {
            long id = Long.parseLong(idString);
            updatedClient = clientService.updateById(id, name, phone);

        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }

        if (updatedClient == null) {
            writer.write("Client has not updated(");
        } else {
            writer.write("Client updated: " + updatedClient.toString());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный DELETE-запрос : /client?id=n
        PrintWriter writer = response.getWriter();
        String idString = request.getParameter("id");


        Client deletedClient = null;
        try {
            long id = Long.parseLong(idString);
            deletedClient = clientService.deleteById(id);

        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }

        if (deletedClient == null) {
            writer.write("Client has not deleted(");
        } else {
            writer.write("Client deleted: " + deletedClient.toString());
        }
    }
}
