package lk.ijse.cloudops.billing.repository;

import lk.ijse.cloudops.billing.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByClientId(UUID clientId);
    List<Subscription> findByStatus(String status);
}
