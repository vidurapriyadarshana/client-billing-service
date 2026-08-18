package lk.ijse.cloudops.billing.controller;

import jakarta.validation.Valid;
import lk.ijse.cloudops.billing.dto.request.ClientRequestDTO;
import lk.ijse.cloudops.billing.dto.response.ClientResponseDTO;
import lk.ijse.cloudops.billing.dto.response.StandardResponse;
import lk.ijse.cloudops.billing.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<StandardResponse<ClientResponseDTO>> createClient(@Valid @RequestBody ClientRequestDTO requestDTO) {
        ClientResponseDTO created = clientService.createClient(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success(HttpStatus.CREATED.value(), "Client created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<ClientResponseDTO>> getClientById(@PathVariable UUID id) {
        ClientResponseDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Client retrieved successfully", client));
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<ClientResponseDTO>>> getAllClients(
            @RequestParam(required = false) String status) {
        List<ClientResponseDTO> clients = clientService.getAllClients(status);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Clients retrieved successfully", clients));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<ClientResponseDTO>> updateClient(
            @PathVariable UUID id,
            @Valid @RequestBody ClientRequestDTO requestDTO) {
        ClientResponseDTO updated = clientService.updateClient(id, requestDTO);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Client updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Void>> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Client deleted successfully", null));
    }
}
