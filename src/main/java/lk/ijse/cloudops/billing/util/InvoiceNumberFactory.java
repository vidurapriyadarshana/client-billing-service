package lk.ijse.cloudops.billing.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InvoiceNumberFactory {

    private final AtomicInteger sequence = new AtomicInteger(1001);

    public synchronized String generateInvoiceNumber() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        int seq = sequence.getAndIncrement();
        return String.format("INV-%s-%04d", dateStr, seq);
    }
}
