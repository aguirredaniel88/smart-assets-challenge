package ai.smartassets.challenge.repository;

import ai.smartassets.challenge.model.Creative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreativeRepository extends MongoRepository<Creative, String> {
    Page<Creative> findByCampaignId(String campaignId, Pageable pageable);
}
