package lk.ijse.cloudops.billing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequestDTO {

    @NotNull(message = "Client ID is required")
    private UUID clientId;

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank(message = "Plan tier is required")
    private String planTier;

    @NotNull(message = "Monthly amount is required")
    @Positive(message = "Monthly amount must be greater than zero")
    private BigDecimal monthlyAmount;

    private String billingCycle; // MONTHLY, YEARLY

    @NotNull(message = "Start date is required")
    private LocalDate startDate;
}
