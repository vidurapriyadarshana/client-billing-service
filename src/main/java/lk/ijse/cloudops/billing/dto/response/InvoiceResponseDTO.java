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
public class InvoiceResponseDTO {
    private UUID id;
    private UUID clientId;
    private String companyName;
    private UUID subscriptionId;
    private String invoiceNumber;
    private BigDecimal amountDue;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String gcsPdfUrl;
    private LocalDateTime createdAt;
}
