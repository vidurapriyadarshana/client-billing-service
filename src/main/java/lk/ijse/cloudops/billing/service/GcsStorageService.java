package lk.ijse.cloudops.billing.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GcsStorageService {

    private final Storage storage;

    @Value("${gcs.bucket-name:cloudops-invoices-dev}")
    private String bucketName;

    public GcsStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadPdf(String objectName, byte[] pdfBytes) {
        String fallbackUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
        try {
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("application/pdf")
                    .build();
            storage.create(blobInfo, pdfBytes);
            log.info("Successfully uploaded PDF to GCS bucket: {}/{}", bucketName, objectName);
            return fallbackUrl;
        } catch (Exception e) {
            log.warn("GCS upload bypassed/failed (local environment fallback): {}", e.getMessage());
            return fallbackUrl;
        }
    }
}
