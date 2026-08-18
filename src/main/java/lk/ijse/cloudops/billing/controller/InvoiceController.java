package lk.ijse.cloudops.billing.controller;

import jakarta.validation.Valid;
import lk.ijse.cloudops.billing.dto.request.InvoiceGenerateRequestDTO;
import lk.ijse.cloudops.billing.dto.response.InvoiceResponseDTO;
import lk.ijse.cloudops.billing.dto.response.StandardResponse;
import lk.ijse.cloudops.billing.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<StandardResponse<InvoiceResponseDTO>> generateInvoice(
            @Valid @RequestBody InvoiceGenerateRequestDTO requestDTO) {
        InvoiceResponseDTO invoice = invoiceService.generateInvoice(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success(HttpStatus.CREATED.value(), "Invoice generated successfully", invoice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<InvoiceResponseDTO>> getInvoiceById(@PathVariable UUID id) {
        InvoiceResponseDTO invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Invoice retrieved successfully", invoice));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<StandardResponse<List<InvoiceResponseDTO>>> getInvoicesByClient(@PathVariable UUID clientId) {
        List<InvoiceResponseDTO> invoices = invoiceService.getInvoicesByClient(clientId);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Invoices retrieved successfully", invoices));
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<InvoiceResponseDTO>>> getAllInvoices(
            @RequestParam(required = false) String status) {
        List<InvoiceResponseDTO> invoices = invoiceService.getAllInvoices(status);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Invoices retrieved successfully", invoices));
    }
}
