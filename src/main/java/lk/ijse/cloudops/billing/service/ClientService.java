package lk.ijse.cloudops.billing.service;

import lk.ijse.cloudops.billing.dto.request.ClientRequestDTO;
import lk.ijse.cloudops.billing.dto.response.ClientResponseDTO;
import lk.ijse.cloudops.billing.entity.Client;
import lk.ijse.cloudops.billing.exception.BadRequestException;
import lk.ijse.cloudops.billing.exception.NotFoundException;
import lk.ijse.cloudops.billing.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO requestDTO) {
        if (clientRepository.findByContactEmail(requestDTO.getContactEmail()).isPresent()) {
            throw new BadRequestException("Client with email " + requestDTO.getContactEmail() + " already exists");
        }

        Client client = Client.builder()
                .companyName(requestDTO.getCompanyName())
                .contactEmail(requestDTO.getContactEmail())
                .contactPhone(requestDTO.getContactPhone())
                .address(requestDTO.getAddress())
                .status("ACTIVE")
                .build();

        Client savedClient = clientRepository.save(client);
        return modelMapper.map(savedClient, ClientResponseDTO.class);
    }

    public ClientResponseDTO getClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
        return modelMapper.map(client, ClientResponseDTO.class);
    }

    public List<ClientResponseDTO> getAllClients(String status) {
        List<Client> clients = (status != null && !status.isBlank())
                ? clientRepository.findByStatus(status)
                : clientRepository.findAll();

        return clients.stream()
                .map(client -> modelMapper.map(client, ClientResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ClientResponseDTO updateClient(UUID id, ClientRequestDTO requestDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));

        client.setCompanyName(requestDTO.getCompanyName());
        client.setContactEmail(requestDTO.getContactEmail());
        client.setContactPhone(requestDTO.getContactPhone());
        client.setAddress(requestDTO.getAddress());

        Client updatedClient = clientRepository.save(client);
        return modelMapper.map(updatedClient, ClientResponseDTO.class);
    }

    @Transactional
    public void deleteClient(UUID id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
}
