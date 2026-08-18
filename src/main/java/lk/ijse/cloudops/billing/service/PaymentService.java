package lk.ijse.cloudops.billing.service;

import lk.ijse.cloudops.billing.dto.request.PaymentRequestDTO;
import lk.ijse.cloudops.billing.dto.response.PaymentResponseDTO;
import lk.ijse.cloudops.billing.entity.Invoice;
import lk.ijse.cloudops.billing.entity.Payment;
import lk.ijse.cloudops.billing.exception.NotFoundException;
import lk.ijse.cloudops.billing.repository.InvoiceRepository;
import lk.ijse.cloudops.billing.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    public PaymentResponseDTO recordPayment(PaymentRequestDTO requestDTO) {
        Invoice invoice = invoiceRepository.findById(requestDTO.getInvoiceId())
                .orElseThrow(() -> new NotFoundException("Invoice not found with id: " + requestDTO.getInvoiceId()));

        Payment payment = Payment.builder()
                .invoice(invoice)
                .amountPaid(requestDTO.getAmountPaid())
                .paymentMethod(requestDTO.getPaymentMethod())
                .transactionReference(requestDTO.getTransactionReference())
                .status("COMPLETED")
                .paymentDate(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Automatically update Invoice status to PAID if payment covers total amount
        if (requestDTO.getAmountPaid().compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus("PAID");
            invoiceRepository.save(invoice);
        }

        return mapToDTO(savedPayment);
    }

    public List<PaymentResponseDTO> getPaymentsByInvoice(UUID invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PaymentResponseDTO mapToDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .invoiceId(payment.getInvoice().getId())
                .invoiceNumber(payment.getInvoice().getInvoiceNumber())
                .amountPaid(payment.getAmountPaid())
                .paymentMethod(payment.getPaymentMethod())
                .transactionReference(payment.getTransactionReference())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
