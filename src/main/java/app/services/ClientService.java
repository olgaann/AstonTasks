package app.services;

import app.entities.Client;
import app.repositories.ClientRepository;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    public Client getById(long id) {
        return clientRepository.getById(id);
    }

    public Client getByName(String name) {
        return clientRepository.getByName(name);
    }

    public List<Integer> getRoomsNumbersByClientId(long id) {
        return clientRepository.getRoomsNumbersByClientId(id);
    }

    public Client add(String name, String phone) throws SQLException {
        return clientRepository.add(name, phone);
    }

    public Client updateById(long id, String name, String phone) {
        return clientRepository.updateById(id, name, phone);
    }

    public Client deleteById(long id) {
        return clientRepository.deleteById(id);
    }
}
