package app.services;

import app.entities.Client;
import app.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    public Optional<Client> getById(long id) {
        return clientRepository.getById(id);
    }

    public List<Client> getByName(String name) {
        return clientRepository.getByName(name);
    }

    public List<Integer> getRoomsNumbersByClientId(long id) {
        return clientRepository.getRoomsNumbersByClientId(id);
    }

    public Optional<Client> add(String name, String phone) {
        return clientRepository.add(name, phone);
    }

    public Optional<Client> updateById(long id, String name, String phone) {
        return clientRepository.updateById(id, name, phone);
    }

    public Optional<Client> deleteById(long id) {
        return clientRepository.deleteById(id);
    }
}
