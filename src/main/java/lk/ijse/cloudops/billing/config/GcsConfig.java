package lk.ijse.cloudops.billing.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcsConfig {

    @Value("${gcs.project-id:cloudops-psa-dev}")
    private String projectId;

    @Bean
    public Storage googleCloudStorage() {
        try {
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .build()
                    .getService();
        } catch (Exception e) {
            // Fallback for offline/local dev environment without GCP service account credentials
            return StorageOptions.getDefaultInstance().getService();
        }
    }
}
