package lk.ijse.cloudops.billing.service;

import lk.ijse.cloudops.billing.dto.request.SubscriptionRequestDTO;
import lk.ijse.cloudops.billing.dto.response.SubscriptionResponseDTO;
import lk.ijse.cloudops.billing.entity.Client;
import lk.ijse.cloudops.billing.entity.Subscription;
import lk.ijse.cloudops.billing.exception.NotFoundException;
import lk.ijse.cloudops.billing.repository.ClientRepository;
import lk.ijse.cloudops.billing.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public SubscriptionResponseDTO createSubscription(SubscriptionRequestDTO requestDTO) {
        Client client = clientRepository.findById(requestDTO.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + requestDTO.getClientId()));

        String billingCycle = (requestDTO.getBillingCycle() != null && !requestDTO.getBillingCycle().isBlank())
                ? requestDTO.getBillingCycle()
                : "MONTHLY";

        LocalDate startDate = requestDTO.getStartDate();
        LocalDate nextBillingDate = "YEARLY".equalsIgnoreCase(billingCycle)
                ? startDate.plusYears(1)
                : startDate.plusMonths(1);

        Subscription subscription = Subscription.builder()
                .client(client)
                .productId(requestDTO.getProductId())
                .planTier(requestDTO.getPlanTier())
                .monthlyAmount(requestDTO.getMonthlyAmount())
                .billingCycle(billingCycle)
                .startDate(startDate)
                .nextBillingDate(nextBillingDate)
                .status("ACTIVE")
                .build();

        Subscription saved = subscriptionRepository.save(subscription);
        return mapToDTO(saved);
    }

    public SubscriptionResponseDTO getSubscriptionById(UUID id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subscription not found with id: " + id));
        return mapToDTO(subscription);
    }

    public List<SubscriptionResponseDTO> getSubscriptionsByClient(UUID clientId) {
        return subscriptionRepository.findByClientId(clientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<SubscriptionResponseDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubscriptionResponseDTO cancelSubscription(UUID id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subscription not found with id: " + id));
        subscription.setStatus("CANCELLED");
        Subscription updated = subscriptionRepository.save(subscription);
        return mapToDTO(updated);
    }

    private SubscriptionResponseDTO mapToDTO(Subscription sub) {
        return SubscriptionResponseDTO.builder()
                .id(sub.getId())
                .clientId(sub.getClient().getId())
                .companyName(sub.getClient().getCompanyName())
                .productId(sub.getProductId())
                .planTier(sub.getPlanTier())
                .monthlyAmount(sub.getMonthlyAmount())
                .billingCycle(sub.getBillingCycle())
                .startDate(sub.getStartDate())
                .nextBillingDate(sub.getNextBillingDate())
                .status(sub.getStatus())
                .createdAt(sub.getCreatedAt())
                .build();
    }
}
