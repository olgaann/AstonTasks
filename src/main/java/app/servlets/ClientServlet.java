package app.servlets;

import app.entities.Client;
import app.repositories.ClientRepository;
import app.services.ClientService;
import org.hibernate.SessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class ClientServlet extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init() {
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("sessionFactory");
        ClientRepository clientRepository = new ClientRepository(sessionFactory);
        clientService = new ClientService(clientRepository);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //Валидные GET-запросы : /client   /client/?id=n  (+ выводит список номеров, забронированных клиентом)
        // /client/?name=clientName

        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        String result = parseRequestURIAndQueryString(requestURI, queryString);
        PrintWriter writer = response.getWriter();
        writer.write("Clients list: \n" + result);
    }

    public String parseRequestURIAndQueryString(String requestURI, String queryString) {
        if (requestURI.equals("/client") || requestURI.equals("/client/")) {
            if (queryString == null) {
                return clientService.getAllClients().toString();
            }
            return parseQueryString(queryString);

        }
        return "Your request is invalid";
    }

    public String parseQueryString(String queryString) {
        try {
            if (queryString.matches("^id=\\d+$")) {
                long id = Long.parseLong(queryString.substring(queryString.indexOf("=") + 1));
                Optional<Client> client = clientService.getById(id);
                if (client.isEmpty()) return "No clients with that id";
                return client.get().toString() + "\n" + client.get().getRooms().toString();
            }
            if (queryString.matches("^name=[a-zA-Z1-9]+$")) {
                String name = queryString.substring(queryString.indexOf("=") + 1);
                List<Client> clients = clientService.getByName(name);
                if (clients.isEmpty()) return "No clients with that id";
                return clients.toString();
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
        if (name == null) {
            writer.write("Your request is invalid");
            return;
        }
        Optional<Client> newClient = clientService.add(name, phone);
        if (newClient.isEmpty()) writer.write("Client has not added");
        writer.write("Client added: " + newClient.get().toString());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный PUT-запрос : /client?id=n&name=newName&phone=newPhone
        PrintWriter writer = response.getWriter();
        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        if (idString == null) {
            writer.write("Your request is invalid");
            return;
        }
        if (name == null && phone == null) {
            writer.write("Your request is invalid: no updated fields");
            return;
        }

        Optional<Client> updatedClient;
        try {
            long id = Long.parseLong(idString);
            updatedClient = clientService.updateById(id, name, phone);

        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }

        if (updatedClient.isEmpty()) {
            writer.write("Client has not updated(");
            return;
        }
        writer.write("Client updated: " + updatedClient.get().toString());

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Валидный DELETE-запрос : /client?id=n
        PrintWriter writer = response.getWriter();
        String idString = request.getParameter("id");

        Optional<Client> deletedClient;
        try {
            long id = Long.parseLong(idString);
            deletedClient = clientService.deleteById(id);
        } catch (NumberFormatException e) {
            writer.write("Your request is invalid");
            return;
        }

        if (deletedClient.isEmpty()) {
            writer.write("Client has not deleted(");
            return;
        }
        writer.write("Client deleted: " + deletedClient.get().toString());

    }
}
