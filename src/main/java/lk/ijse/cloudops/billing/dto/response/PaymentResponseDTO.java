package lk.ijse.cloudops.billing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private UUID id;
    private UUID invoiceId;
    private String invoiceNumber;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private String transactionReference;
    private String status;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
}
