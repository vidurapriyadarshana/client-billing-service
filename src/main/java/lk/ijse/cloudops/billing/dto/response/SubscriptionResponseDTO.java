package lk.ijse.cloudops.billing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDTO {
    private UUID id;
    private UUID clientId;
    private String companyName;
    private String productId;
    private String planTier;
    private BigDecimal monthlyAmount;
    private String billingCycle;
    private LocalDate startDate;
    private LocalDate nextBillingDate;
    private String status;
    private LocalDateTime createdAt;
}
