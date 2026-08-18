package lk.ijse.cloudops.billing.controller;

import jakarta.validation.Valid;
import lk.ijse.cloudops.billing.dto.request.PaymentRequestDTO;
import lk.ijse.cloudops.billing.dto.response.PaymentResponseDTO;
import lk.ijse.cloudops.billing.dto.response.StandardResponse;
import lk.ijse.cloudops.billing.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<StandardResponse<PaymentResponseDTO>> recordPayment(
            @Valid @RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO payment = paymentService.recordPayment(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success(HttpStatus.CREATED.value(), "Payment recorded successfully", payment));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<StandardResponse<List<PaymentResponseDTO>>> getPaymentsByInvoice(@PathVariable UUID invoiceId) {
        List<PaymentResponseDTO> payments = paymentService.getPaymentsByInvoice(invoiceId);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Payments retrieved successfully", payments));
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<PaymentResponseDTO>>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Payments retrieved successfully", payments));
    }
}
