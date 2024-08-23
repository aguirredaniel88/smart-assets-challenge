package ai.smartassets.challenge.repository;

import ai.smartassets.challenge.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampaignRepository extends MongoRepository<Campaign, String> {

    Page<Campaign> findByBrandId(String brandId, Pageable pageable);
}
