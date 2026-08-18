package lk.ijse.cloudops.billing.util;

import lk.ijse.cloudops.billing.entity.Invoice;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PdfGenerator {

    public byte[] generateInvoicePdf(Invoice invoice) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                
                PDType1Font helveticaBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                contentStream.setFont(helveticaBold, 20);
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("CloudOps PSA — INVOICE");

                contentStream.setFont(helveticaBold, 12);
                contentStream.newLineAtOffset(0, -30);
                contentStream.showText("Invoice Number: " + invoice.getInvoiceNumber());

                contentStream.setFont(helvetica, 10);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Issue Date: " + invoice.getIssueDate());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Due Date: " + invoice.getDueDate());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Status: " + invoice.getStatus());

                contentStream.newLineAtOffset(0, -30);
                contentStream.setFont(helveticaBold, 12);
                contentStream.showText("Billed To:");
                contentStream.setFont(helvetica, 10);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Client ID: " + invoice.getClient().getId());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Company: " + invoice.getClient().getCompanyName());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Email: " + invoice.getClient().getContactEmail());

                contentStream.newLineAtOffset(0, -35);
                contentStream.setFont(helveticaBold, 12);
                contentStream.showText("Financial Summary:");
                contentStream.setFont(helvetica, 10);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Amount Due: $" + invoice.getAmountDue());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Tax Amount: $" + invoice.getTaxAmount());
                contentStream.newLineAtOffset(0, -15);
                contentStream.setFont(helveticaBold, 12);
                contentStream.showText("Total Amount: $" + invoice.getTotalAmount());

                contentStream.endText();
            }

            document.save(baos);
            return baos.toByteArray();
        }
    }
}
