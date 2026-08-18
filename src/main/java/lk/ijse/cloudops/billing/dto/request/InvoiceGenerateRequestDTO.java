package lk.ijse.cloudops.billing.dto.request;

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
public class InvoiceGenerateRequestDTO {

    @NotNull(message = "Client ID is required")
    private UUID clientId;

    private UUID subscriptionId;

    @NotNull(message = "Amount due is required")
    @Positive(message = "Amount due must be greater than zero")
    private BigDecimal amountDue;

    private BigDecimal taxAmount;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
