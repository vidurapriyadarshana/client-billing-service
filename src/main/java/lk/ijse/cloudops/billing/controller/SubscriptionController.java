package lk.ijse.cloudops.billing.controller;

import jakarta.validation.Valid;
import lk.ijse.cloudops.billing.dto.request.SubscriptionRequestDTO;
import lk.ijse.cloudops.billing.dto.response.StandardResponse;
import lk.ijse.cloudops.billing.dto.response.SubscriptionResponseDTO;
import lk.ijse.cloudops.billing.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<StandardResponse<SubscriptionResponseDTO>> createSubscription(
            @Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        SubscriptionResponseDTO created = subscriptionService.createSubscription(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success(HttpStatus.CREATED.value(), "Subscription created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<SubscriptionResponseDTO>> getSubscriptionById(@PathVariable UUID id) {
        SubscriptionResponseDTO sub = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Subscription retrieved successfully", sub));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<StandardResponse<List<SubscriptionResponseDTO>>> getSubscriptionsByClient(
            @PathVariable UUID clientId) {
        List<SubscriptionResponseDTO> subs = subscriptionService.getSubscriptionsByClient(clientId);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Subscriptions retrieved successfully", subs));
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<SubscriptionResponseDTO>>> getAllSubscriptions() {
        List<SubscriptionResponseDTO> subs = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Subscriptions retrieved successfully", subs));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<StandardResponse<SubscriptionResponseDTO>> cancelSubscription(@PathVariable UUID id) {
        SubscriptionResponseDTO cancelled = subscriptionService.cancelSubscription(id);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Subscription cancelled successfully", cancelled));
    }
}
