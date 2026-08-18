package lk.ijse.cloudops.billing.service;

import lk.ijse.cloudops.billing.dto.request.InvoiceGenerateRequestDTO;
import lk.ijse.cloudops.billing.dto.response.InvoiceResponseDTO;
import lk.ijse.cloudops.billing.entity.Client;
import lk.ijse.cloudops.billing.entity.Invoice;
import lk.ijse.cloudops.billing.entity.Subscription;
import lk.ijse.cloudops.billing.exception.NotFoundException;
import lk.ijse.cloudops.billing.repository.ClientRepository;
import lk.ijse.cloudops.billing.repository.InvoiceRepository;
import lk.ijse.cloudops.billing.repository.SubscriptionRepository;
import lk.ijse.cloudops.billing.util.InvoiceNumberFactory;
import lk.ijse.cloudops.billing.util.PdfGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceNumberFactory invoiceNumberFactory;
    private final PdfGenerator pdfGenerator;
    private final GcsStorageService gcsStorageService;

    @Transactional
    public InvoiceResponseDTO generateInvoice(InvoiceGenerateRequestDTO requestDTO) {
        Client client = clientRepository.findById(requestDTO.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + requestDTO.getClientId()));

        Subscription subscription = null;
        if (requestDTO.getSubscriptionId() != null) {
            subscription = subscriptionRepository.findById(requestDTO.getSubscriptionId()).orElse(null);
        }

        String invoiceNumber = invoiceNumberFactory.generateInvoiceNumber();
        BigDecimal taxAmount = requestDTO.getTaxAmount() != null ? requestDTO.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal totalAmount = requestDTO.getAmountDue().add(taxAmount);

        Invoice invoice = Invoice.builder()
                .client(client)
                .subscription(subscription)
                .invoiceNumber(invoiceNumber)
                .amountDue(requestDTO.getAmountDue())
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .status("UNPAID")
                .issueDate(LocalDate.now())
                .dueDate(requestDTO.getDueDate())
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // Generate PDF and upload to GCS
        try {
            byte[] pdfBytes = pdfGenerator.generateInvoicePdf(savedInvoice);
            String objectName = String.format("invoices/%s.pdf", invoiceNumber);
            String gcsUrl = gcsStorageService.uploadPdf(objectName, pdfBytes);
            savedInvoice.setGcsPdfUrl(gcsUrl);
            savedInvoice = invoiceRepository.save(savedInvoice);
        } catch (Exception e) {
            log.error("Failed to generate/upload invoice PDF: {}", e.getMessage(), e);
        }

        return mapToDTO(savedInvoice);
    }

    public InvoiceResponseDTO getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with id: " + id));
        return mapToDTO(invoice);
    }

    public List<InvoiceResponseDTO> getInvoicesByClient(UUID clientId) {
        return invoiceRepository.findByClientId(clientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<InvoiceResponseDTO> getAllInvoices(String status) {
        List<Invoice> invoices = (status != null && !status.isBlank())
                ? invoiceRepository.findByStatus(status)
                : invoiceRepository.findAll();

        return invoices.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private InvoiceResponseDTO mapToDTO(Invoice invoice) {
        return InvoiceResponseDTO.builder()
                .id(invoice.getId())
                .clientId(invoice.getClient().getId())
                .companyName(invoice.getClient().getCompanyName())
                .subscriptionId(invoice.getSubscription() != null ? invoice.getSubscription().getId() : null)
                .invoiceNumber(invoice.getInvoiceNumber())
                .amountDue(invoice.getAmountDue())
                .taxAmount(invoice.getTaxAmount())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .gcsPdfUrl(invoice.getGcsPdfUrl())
                .createdAt(invoice.getCreatedAt())
                .build();
    }
}
